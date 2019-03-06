package ca.gl.StockExchange.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.gl.StockExchange.dao.StockHistoryListRepository;
import ca.gl.StockExchange.dao.StockRepository;
import ca.gl.fileUploader.model.Stock;
import ca.gl.fileUploader.model.StockHistory;
import ca.gl.fileUploader.model.StockHistoryList;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StockService {
	@Autowired
	private StockRepository stockRepo;
	
	@Autowired
	private StockHistoryListRepository stockHisListRepo;
	
	public Flux<Stock> getLatestStocks() {
		return stockRepo.findAllLatest();
	}

	/**
	 * Get today's history
	 * @param stockHistoryId
	 * @return
	 */
	public Mono<StockHistoryList> getTodaysHistory(String stockHistoryId) {
		return stockHisListRepo.findById(stockHistoryId).flatMap(todaysHistory()).switchIfEmpty(Mono.empty());
	}

	/**
	 * Filter todays history from week history
	 * @return
	 */
	private Function<? super StockHistoryList, ? extends Mono<? extends StockHistoryList>> todaysHistory() {
		return stockHisList ->  {
			List<StockHistory> 	todaysList=stockHisList.getStockList().stream().filter(sh -> sh.getDate().getDayOfYear() ==LocalDateTime.now().getDayOfYear()).collect(Collectors.toList());
			Collections.sort(todaysList);
			stockHisList.setStockList(todaysList);
			stockHisList.setCount(todaysList.size());
			return Mono.just(stockHisList);
		};
	}

	/**Get week history
	 * @param stockHistoryId
	 * @return
	 */
	public Mono<StockHistoryList> getWeeksHistory(String stockHistoryId) {
		return stockHisListRepo.findById(stockHistoryId).switchIfEmpty(Mono.empty());
		}
}
