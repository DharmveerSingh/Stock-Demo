package ca.gl.fileUploader.Schedular;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ca.gl.fileUploader.constant.AppConstants;
import ca.gl.fileUploader.dao.StockHistoryListRepository;
import ca.gl.fileUploader.dao.StockRepository;
import ca.gl.fileUploader.model.Stock;
import ca.gl.fileUploader.model.StockHistoryList;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Scheduled task class.
 *
 * @author dharamveer.singh
 */
@Component
public class ScheduledTasks {

	/** The history list repo. */
	@Autowired
	private StockHistoryListRepository historyListRepo;

	/** The stock repo. */
	@Autowired
	private StockRepository stockRepo;

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	/**
	 * this method will run at 10 PM of every weekday to store historycal data about
	 * stocks.
	 */
	@Scheduled(cron = "0 0 22 * * MON-FRI")
	public void reportCurrentTime() {

		Flux<Stock> data = stockRepo.findAllLatest();
		data.doOnNext(stock -> insertIntoDB(stock)).doOnError(err -> err.printStackTrace()).subscribe();
	}

	/**
	 * Insert stock data to db.
	 *
	 * @param stock the stock
	 */
	private void insertIntoDB(Stock stock) {
		Mono<StockHistoryList> stockHistory = historyListRepo
				.findById(AppConstants.DAILY_HISTORY + stock.getStockSymbol());
		stockHistory.doOnNext(appedHistory(stock)).switchIfEmpty(historyListRepo.save(stock.toHistoryList()))
				.doOnNext(d -> log.info("Data going to save is: " + d)).subscribe();

	}

	/**
	 * Apped history.
	 *
	 * @param stock the stock
	 * @return the consumer<? super stock history list>
	 */
	private Consumer<? super StockHistoryList> appedHistory(Stock stock) {
		return data -> {
			data.getStockList().add(stock.toHistory());
			data.setCount(data.getCount() + 1);
			historyListRepo.save(data).doOnNext(d -> log.info("Appeding data history: " + data.getStockId()))
					.subscribe();
		};
	}
}