package ca.gl.StockExchange.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.gl.StockExchange.service.StockService;
import ca.gl.fileUploader.model.Stock;
import ca.gl.fileUploader.model.StockHistoryList;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Stock Controller
 * @author dharamveer.singh
 *
 */
@RestController
@RequestMapping("/stock")
public class StockController {

	private static final Logger log = LoggerFactory.getLogger(StockController.class);
	@Autowired
	private StockService stockService;

	@GetMapping("/latest")
	public Flux<Stock> getStocks() {
		log.info("Called getAllLateststocks");
		return stockService.getLatestStocks();
	}
	
	@GetMapping("/today")
	public Mono<StockHistoryList> getTodaysHistory(@RequestParam("stockHistoryId")String stockHistoryId){
		return stockService.getTodaysHistory(stockHistoryId);
	}
	
	@GetMapping("/weekly")
	public Mono<StockHistoryList> getWeeksHistory(@RequestParam("stockHistoryId")String stockHistoryId){
		return stockService.getWeeksHistory(stockHistoryId);
	}
}
