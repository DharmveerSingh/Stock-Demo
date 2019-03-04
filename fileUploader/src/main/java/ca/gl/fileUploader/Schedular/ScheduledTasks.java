package ca.gl.fileUploader.Schedular;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ca.gl.fileUploader.dao.StockHistoryListRepository;
import ca.gl.fileUploader.dao.StockRepository;
import ca.gl.fileUploader.model.Stock;
import ca.gl.fileUploader.model.StockHistoryList;
import constant.AppConstants;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ScheduledTasks {

	@Autowired
	private StockHistoryListRepository historyListRepo;

	@Autowired
	private StockRepository stockRepo;

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	 @Scheduled(cron = "0 0 22 * * MON-FRI")
	//@Scheduled(initialDelay = 10000, fixedRate = 5000)
	public void reportCurrentTime() {

		Flux<Stock> data = stockRepo.findAllLatest();
		data.doOnNext(stock -> insertIntoDB(stock)).doOnError(err -> err.printStackTrace()).subscribe();
	}

	private void insertIntoDB(Stock stock) {
		Mono<StockHistoryList> stockHistory = historyListRepo.findById(AppConstants.DAILY_HISTORY + stock.getStockSymbol());
		stockHistory.doOnNext(data -> {
			data.getStockList().add(stock.toHistory());
			data.setCount(data.getCount()+1);
			historyListRepo.save(data).doOnNext(d -> log.info("Appeding data history: " + data.getStockId()))
					.subscribe();
		}).switchIfEmpty(historyListRepo.save(stock.toHistoryList()))
				.doOnNext(d -> log.info("Data going to save is: " + d)).subscribe();

	}
}