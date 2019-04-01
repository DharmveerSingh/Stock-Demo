package com.gl.feign.stock;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.gl.fus.model.Stock;
import ca.gl.fus.model.StockHistoryList;

/**
 * The Interface StockServiceProxy.
 */
@FeignClient(value = "srs", fallback = StockFallbackServiceProxy.class)
public interface StockServiceProxy {

	/**
	 * Gets the latest stocks.
	 *
	 * @return the latest stocks
	 */
	@GetMapping("/stock/latest")
	public List<Stock> getLatestStocks();

	/**
	 * Gets the todays history.
	 *
	 * @param stockHistoryId the stock history id
	 * @return the todays history
	 */
	@GetMapping("/stock/today")
	public StockHistoryList getTodaysHistory(@RequestParam("stockHistoryId") String stockHistoryId);

	/**
	 * Gets the weeks history.
	 *
	 * @param stockHistoryId the stock history id
	 * @return the weeks history
	 */
	@GetMapping("/stock/weekly")
	public StockHistoryList getWeeksHistory(@RequestParam("stockHistoryId") String stockHistoryId);

	/**
	 * Gets the salable stocks.
	 *
	 * @return the salable stocks
	 */
	@GetMapping("/stock/salable")
	public List<ca.gl.user.model.transaction.Stock> getSalableStocks();
}
