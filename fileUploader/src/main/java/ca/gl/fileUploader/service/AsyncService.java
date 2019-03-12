package ca.gl.fileUploader.service;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.error.CASMismatchException;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;

import ca.gl.fileUploader.dao.StockHistoryListRepository;
import ca.gl.fileUploader.dao.StockRepository;
import ca.gl.fileUploader.model.Stock;
import ca.gl.fileUploader.model.StockHistoryList;
import constant.AppConstants;
import reactor.core.publisher.Mono;

// TODO: Auto-generated Javadoc
/**
 * The Class AsyncService.
 */
@Service
public class AsyncService {

	/** This async method save latest stocks and will also update the same in their history. */
	private static final Logger log = LoggerFactory.getLogger(AsyncService.class);
	
	/** The stock repo. */
	@Autowired
	private StockRepository stockRepo;

	/** The list repo. */
	@Autowired
	private StockHistoryListRepository listRepo;

	/**
	 * Save stock list.
	 *
	 * @param stockList the stock list
	 */
	@Async
	public void saveStocksUpdateHis(List<Stock> stockList) {
		stockRepo.saveAll(stockList).subscribe();
		stockList.stream().parallel().forEach(stock -> {
			final String stockHisId = AppConstants.REGULAR_HISTORY + stock.getStockSymbol();
			saveOrUpdateRegularHist(stock, stockHisId);
		});
	}

	/**
	 * Save or update regular history.
	 *
	 * @param stock the stock
	 * @param stockHisId the stock his id
	 */
	private void saveOrUpdateRegularHist(final Stock stock, final String stockHisId) {
		Mono<StockHistoryList> stockHistory = listRepo.findById(stockHisId);
		stockHistory.flatMap(updateStockHisList(stock, stockHisId)).switchIfEmpty(createStockHisList(stock, stockHisId))
				.subscribe();
	}

	/**
	 * Update stock history list.
	 *
	 * @param stock the stock
	 * @param stockHisId the stock his id
	 * @return the function<? super stock history list,? extends mono<? extends stock history list>>
	 */
	private Function<? super StockHistoryList, ? extends Mono<? extends StockHistoryList>> updateStockHisList(
			final Stock stock, final String stockHisId) {
		return data -> {
			log.info("{} :for key got stock history -{}", data.getStockId(), data.getVersion());
			data.getStockList().add(stock.toHistory());
			data.setCount(data.getCount() + 1);
			return listRepo.save(data).doOnError(handleUpdationErrors(stock, stockHisId));
		};
	}

	/**
	 * create stock history list.
	 *
	 * @param stock the stock
	 * @param stockHisId the stock his id
	 * @return the mono
	 */
	private Mono<StockHistoryList> createStockHisList(final Stock stock, final String stockHisId) {
		return listRepo.save(stock.toRegularHistoryList()).doOnError(handleCreationErrors(stock, stockHisId));
	}

	/**
	 * Handle creation errors.
	 *
	 * @param stock the stock
	 * @param stockHisId the stock his id
	 * @return the consumer<? super throwable>
	 */
	private Consumer<? super Throwable> handleCreationErrors(final Stock stock, final String stockHisId) {
		return err -> {
			log.error("Got error while Saving: {}, , errorclass {}", err.getCause(), err.getLocalizedMessage());
			if (err.getCause() instanceof CASMismatchException
					|| err.getCause() instanceof DocumentAlreadyExistsException) {
				saveOrUpdateRegularHist(stock, stockHisId);
			}
		};
	}

	/**
	 * Handle updation errors.
	 *
	 * @param stock the stock
	 * @param stockHisId the stock his id
	 * @return the consumer<? super throwable>
	 */
	private Consumer<? super Throwable> handleUpdationErrors(final Stock stock, final String stockHisId) {
		return err -> {
			log.error("Got error while updating: {}, , errorclass {}", err.getCause(), err.getLocalizedMessage());
			if (err.getCause() instanceof CASMismatchException) {
				saveOrUpdateRegularHist(stock, stockHisId);
			}
		};
	}
}
