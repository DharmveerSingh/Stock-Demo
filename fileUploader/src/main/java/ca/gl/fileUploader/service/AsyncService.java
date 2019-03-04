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

@Service
public class AsyncService {

	/**
	 * This async method save latest stocks and will also update the same in their
	 * history
	 */
	private static final Logger log = LoggerFactory.getLogger(AsyncService.class);
	@Autowired
	private StockRepository stockRepo;

	@Autowired
	private StockHistoryListRepository listRepo;

	@Async
	public void saveStocksUpdateHis(List<Stock> stockList) {
		stockRepo.saveAll(stockList).subscribe();
		stockList.stream().parallel().forEach(stock -> {
			final String stockHisId = AppConstants.REGULAR_HISTORY + stock.getStockSymbol();
			saveOrUpdateRegularHist(stock, stockHisId);
		});
	}

	private void saveOrUpdateRegularHist(final Stock stock, final String stockHisId) {
		Mono<StockHistoryList> stockHistory = listRepo.findById(stockHisId);
		stockHistory.flatMap(updateStockHisList(stock, stockHisId)).switchIfEmpty(createStockHisList(stock, stockHisId))
				.subscribe();
	}

	private Function<? super StockHistoryList, ? extends Mono<? extends StockHistoryList>> updateStockHisList(
			final Stock stock, final String stockHisId) {
		return data -> {
			log.info("{} :for key got stock history -{}", data.getStockId(), data.getVersion());
			data.getStockList().add(stock.toHistory());
			data.setCount(data.getCount() + 1);
			return listRepo.save(data).doOnError(handleUpdationErrors(stock, stockHisId));
		};
	}

	private Mono<StockHistoryList> createStockHisList(final Stock stock, final String stockHisId) {
		return listRepo.save(stock.toRegularHistoryList()).doOnError(handleCreationErrors(stock, stockHisId));
	}

	private Consumer<? super Throwable> handleCreationErrors(final Stock stock, final String stockHisId) {
		return err -> {
			log.error("Got error while Saving: {}, , errorclass {}", err.getCause(), err.getLocalizedMessage());
			if (err.getCause() instanceof CASMismatchException
					|| err.getCause() instanceof DocumentAlreadyExistsException) {
				saveOrUpdateRegularHist(stock, stockHisId);
			}
		};
	}

	private Consumer<? super Throwable> handleUpdationErrors(final Stock stock, final String stockHisId) {
		return err -> {
			log.error("Got error while updating: {}, , errorclass {}", err.getCause(), err.getLocalizedMessage());
			if (err.getCause() instanceof CASMismatchException) {
				saveOrUpdateRegularHist(stock, stockHisId);
			}
		};
	}
}
