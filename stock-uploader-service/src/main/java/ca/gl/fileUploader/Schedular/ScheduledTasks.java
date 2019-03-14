package ca.gl.fileUploader.Schedular;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ca.gl.fileUploader.constant.AppConstants;
import ca.gl.fileUploader.dao.StockHistoryListRepository;
import ca.gl.fileUploader.dao.StockRepository;
import ca.gl.fileUploader.model.Stock;
import ca.gl.fileUploader.model.StockHistory;
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
	 * reecordTodaysHistory this method will run at 10 PM of every weekday to store
	 * historycal data about stocks.
	 */
	@Scheduled(cron = "0 0 11 * * MON-FRI")
	public void reecordTodaysHistory() {
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

	/**
	 * Cron job should be executed every end of the day(right now its 11 AM) at once
	 * to remove all stock history before 1 week
	 */
	@Scheduled(cron = "0 0 11 * * MON-FRI")
	public void removeBeforeWeekHistory() {
		final LocalDateTime date = LocalDateTime.now().minusDays(7l);
		log.info("Week before date: {}", date);
		historyListRepo.getHistoryForAll().flatMap(removeOldHistory(date))
				.doOnError(err -> log.error("*************getting error while saving:{} " + err.getCause()))
				.subscribe();
	}

	/**
	 * Remove old history from weekly history of stocks
	 * 
	 * @param date
	 * @return
	 */
	private Function<? super StockHistoryList, ? extends Publisher<? extends StockHistoryList>> removeOldHistory(
			final LocalDateTime date) {
		return stockHistoryList -> {
			return saveUpdateHistory(date, stockHistoryList);
		};
	}

	/**
	 * Save updated weekly history for stock
	 * 
	 * @param date
	 * @param shl
	 * @return
	 */
	private Publisher<? extends StockHistoryList> saveUpdateHistory(final LocalDateTime date, StockHistoryList shl) {
		List<StockHistory> list = shl.getStockList().stream().filter(isHistoryWithingWeek(date))
				.collect(Collectors.toList());
		shl.setStockList(list);
		shl.setCount(list.size());
		return historyListRepo.save(shl);
	}

	/**
	 * predicate to select if history is of within week
	 * 
	 * @param date
	 * @return Predicate<StockHistory>.. true if date is before
	 */
	public static Predicate<StockHistory> isHistoryWithingWeek(LocalDateTime date) {
		return stockHistory -> stockHistory.getDate().isAfter(date);
	}
}