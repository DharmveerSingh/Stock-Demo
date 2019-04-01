package ca.gl.srs.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.error.CASMismatchException;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;

import ca.gl.fus.model.Stock;
import ca.gl.fus.model.StockHistory;
import ca.gl.fus.model.StockHistoryList;
import ca.gl.srs.dao.StockHistoryListRepository;
import ca.gl.srs.dao.StockRepository;
import ca.gl.srs.dao.transaction.StockTransactionRepository;
import ca.gl.srs.responses.transaction.StockResponse;
import ca.gl.user.model.transaction.Error;
import ca.gl.user.model.transaction.Status;
import ca.gl.user.model.transaction.StockStatus;
import ca.gl.user.model.transaction.StockTransaction;
import ca.gl.user.model.transaction.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The Class StockService.
 */
@Service
public class StockService {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(StockService.class);

	/** The stock repo. */
	@Autowired
	private StockRepository stockRepo;

	/** The stock his list repo. */
	@Autowired
	private StockHistoryListRepository stockHisListRepo;

	/** The t stock repo. */
	@Autowired
	private ca.gl.srs.dao.transaction.TransactionStockRepository tStockRepo;

	/** The transaction repo. */
	@Autowired
	private StockTransactionRepository transactionRepo;

	/**
	 * Gets the latest stocks.
	 *
	 * @return the latest stocks
	 */
	public Flux<Stock> getLatestStocks() {
		return stockRepo.findAllLatest();
	}

	/**
	 * Get today's history.
	 *
	 * @param stockHistoryId the stock history id
	 * @return the todays history
	 */
	public Mono<StockHistoryList> getTodaysHistory(String stockHistoryId) {
		return stockHisListRepo.findById(stockHistoryId).flatMap(todaysHistory()).switchIfEmpty(Mono.empty());
	}

	/**
	 * Filter todays history from week history.
	 *
	 * @return the function<? super stock history list,? extends mono<? extends
	 *         stock history list>>
	 */
	private Function<? super StockHistoryList, ? extends Mono<? extends StockHistoryList>> todaysHistory() {
		return stockHisList -> {
			List<StockHistory> todaysList = stockHisList.getStockList().stream()
					.filter(sh -> sh.getDate().getDayOfYear() == LocalDateTime.now().getDayOfYear())
					.collect(Collectors.toList());
			Collections.sort(todaysList);
			stockHisList.setStockList(todaysList);
			stockHisList.setCount(todaysList.size());
			return Mono.just(stockHisList);
		};
	}

	/**
	 * Get week history.
	 *
	 * @param stockHistoryId the stock history id
	 * @return the weeks history
	 */
	public Mono<StockHistoryList> getWeeksHistory(String stockHistoryId) {
		return stockHisListRepo.findById(stockHistoryId).switchIfEmpty(Mono.empty());
	}

	/**
	 * Purchase.
	 *
	 * @param transaction the transaction
	 * @return the mono
	 */
	public Mono<Transaction> purchase(final Transaction transaction) {
		return reserveStock(transaction).flatMap(createStockTransaction(transaction));
	}

	/**
	 * Creates the stock transaction.
	 *
	 * @param transaction the transaction
	 * @return the function<? super stock response,? extends mono<? extends transaction>>
	 */
	private Function<? super StockResponse, ? extends Mono<? extends Transaction>> createStockTransaction(
			final Transaction transaction) {
		return stockResponse -> {
			if (stockResponse.isAvailable()) {
				if (stockResponse.isReserved()) {
					saveTransaction(transaction, stockResponse);
				} else if (stockResponse.isError()) {

					if (stockResponse.isCASError()) {
						return purchase(transaction);
					} else {
						transaction.setStatus(Status.ERROR);
						transaction.setError(Error.UNKNOWN);
						transaction.setErrorMsg(stockResponse.getThrowable().getCause().toString());
					}
				}
			} else {
				transaction.setStatus(Status.OUT_OF_STOCK);
			}
			return Mono.just(transaction);
		};
	}

	/**
	 * Save transaction.
	 *
	 * @param transaction the transaction
	 * @param stockResponse the stock response
	 */
	private void saveTransaction(final Transaction transaction, StockResponse stockResponse) {
		StockTransaction st = new StockTransaction();
		st.setId(transaction.getId());
		st.setStatus(StockStatus.SUCCESS);
		st.setStockId(transaction.getStockId());
		st.setNumberOfStock(transaction.getCount());
		transaction.setBill(stockResponse.getStock().getUnitPrice() * transaction.getCount());
		transactionRepo.save(st).subscribe();
		transaction.setStatus(Status.STOCK_DONE);
	}

	/**
	 * Reserve stock.
	 *
	 * @param transaction the transaction
	 * @return the mono
	 */
	private Mono<StockResponse> reserveStock(final Transaction transaction) {
		return tStockRepo.findById(transaction.getStockId()).flatMap(stock -> {
			StockResponse stockResponse = new StockResponse();
			stockResponse.setStock(stock);
			if (stock.getTotalCount() >= transaction.getCount()) {
				return updateStock(transaction, stock, stockResponse);
			} else {
				stockResponse.setAvailable(Boolean.FALSE);
			}
			return Mono.just(stockResponse);

		});
	}

	/**
	 * Update stock.
	 *
	 * @param transaction the transaction
	 * @param stock the stock
	 * @param stockResponse the stock response
	 * @return the mono<? extends stock response>
	 */
	private Mono<? extends StockResponse> updateStock(final Transaction transaction,
			ca.gl.user.model.transaction.Stock stock, StockResponse stockResponse) {
		stockResponse.setAvailable(Boolean.TRUE);
		transaction.setStockPrice(stock.getUnitPrice());
		stock.setTotalCount(stock.getTotalCount() - transaction.getCount());
		stock.setModified(LocalDateTime.now());
		return tStockRepo.save(stock).map(stk -> {
			stockResponse.setReserved(Boolean.TRUE);
			return stockResponse;
		}).doOnError(error -> retryIfCASException(error, stockResponse, transaction));
	}


	/**
	 * Retry if CAS exception.
	 *
	 * @param error the error
	 * @param stockResponse the stock response
	 * @param transaction the transaction
	 * @return the mono
	 */
	private Mono<StockResponse> retryIfCASException(Throwable error, StockResponse stockResponse,
			Transaction transaction) {
		log.error("Error {}  while updating stock for transaction: {}", error.getCause(), transaction.getId());
		stockResponse.setError(Boolean.TRUE);
		stockResponse.setThrowable(error);
		if (error instanceof CASMismatchException || error instanceof DocumentAlreadyExistsException) {
			stockResponse.setCASError(Boolean.TRUE);
		} else {
			stockResponse.setCASError(Boolean.FALSE);
		}
		return Mono.just(stockResponse);
	}

	/**
	 * Gets the salable stocks.
	 *
	 * @return the salable stocks
	 */
	public Flux<ca.gl.user.model.transaction.Stock> getSalableStocks() {
		return tStockRepo.findAllSalable();
	}

	/**
	 * Rollback.
	 *
	 * @param transaction the transaction
	 */
	public void rollback(Transaction transaction) {
		transactionRepo.findById(transaction.getId()).flatMap(stockTran -> 
			 tStockRepo.findById(stockTran.getStockId()).flatMap(stock -> {
				stock.setTotalCount(stock.getTotalCount() + stockTran.getNumberOfStock());
				stock.setModified(LocalDateTime.now());
				if (stockTran.getStatus() != StockStatus.PAYMENT_FAILED)
					return tStockRepo.save(stock);
				else
					return Mono.empty();
			}).flatMap(savedStock -> {
				stockTran.setStatus(StockStatus.PAYMENT_FAILED);
				return transactionRepo.save(stockTran);
			})
		).doOnError(err -> rollback(transaction)).subscribe();

	}

}
