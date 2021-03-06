package ca.gl.srs.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.gl.fus.model.Stock;
import ca.gl.fus.model.StockHistoryList;
import ca.gl.srs.service.StockService;
import ca.gl.user.model.transaction.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Stock Controller.
 *
 * @author dharamveer.singh
 */
@RestController
@RequestMapping("/stock")
public class StockController {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(StockController.class);
	
	/** The stock service. */
	@Autowired
	private StockService stockService;

	/**
	 * Gets the stocks.
	 *
	 * @return the stocks
	 */
	@GetMapping("/latest")
	public Flux<Stock> getStocks() {
		log.info("Called getAllLateststocks");
		return stockService.getLatestStocks();
	}
	
	/**
	 * Gets the todays history.
	 *
	 * @param stockHistoryId the stock history id
	 * @return the todays history
	 */
	@GetMapping("/today")
	public Mono<StockHistoryList> getTodaysHistory(@RequestParam("stockHistoryId")String stockHistoryId){
		log.info("Called getTodaysHistory");
		return stockService.getTodaysHistory(stockHistoryId);
	}
	
	/**
	 * Gets the weeks history.
	 *
	 * @param stockHistoryId the stock history id
	 * @return the weeks history
	 */
	@GetMapping("/weekly")
	public Mono<StockHistoryList> getWeeksHistory(@RequestParam("stockHistoryId")String stockHistoryId){
		log.info("Called getTodaysHistory");
		return stockService.getWeeksHistory(stockHistoryId);
	}
	
	/**
	 * Purchase.
	 *
	 * @param transaction the transaction
	 * @return the mono
	 */
	@PostMapping("/purchase")
	public Mono<Transaction> purchase(@RequestBody Transaction transaction){
		log.info("Called purchase");
		return stockService.purchase(transaction);
	}
	
	/**
	 * Gets the salable stocks.
	 *
	 * @return the salable stocks
	 */
	@GetMapping("/salable")
	public Flux<ca.gl.user.model.transaction.Stock> getSalableStocks(){
		log.info("Called getSalableStocks");
			return stockService.getSalableStocks();
		
	}
}
