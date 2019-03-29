package com.gl.feign.stock;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ca.gl.fus.constant.AppConstants;
import ca.gl.fus.model.Stock;
import ca.gl.fus.model.StockHistory;
import ca.gl.fus.model.StockHistoryList;
import feign.FeignException;

/**
 * The Class StockFallbackServiceProxy.
 */
@Component
public class StockFallbackServiceProxy implements StockServiceProxy {

	/** The Constant STOCK_SERVICE_ERROR. */
	private static final String STOCK_SERVICE_ERROR = "Not able to found Stock Service.. falling back to default method";

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(StockFallbackServiceProxy.class);

	/** The cause. */
	private final Throwable cause;

	/**
	 * Instantiates a new stock fallback service proxy.
	 *
	 * @param cause the cause
	 */
	public StockFallbackServiceProxy(Throwable cause) {
		this.cause = cause;
	}

	/* (non-Javadoc)
	 * @see com.gl.feign.stock.StockServiceProxy#getLatestStocks()
	 */
	@Override
	public List<Stock> getLatestStocks() {
		printIfNotFound();
		log.error(STOCK_SERVICE_ERROR);
		return new ArrayList<>();
	}

	/* (non-Javadoc)
	 * @see com.gl.feign.stock.StockServiceProxy#getTodaysHistory(java.lang.String)
	 */
	@Override
	public StockHistoryList getTodaysHistory(String stockHistoryId) {
		printIfNotFound();
		log.error(STOCK_SERVICE_ERROR);

		StockHistoryList todaysHistory = new StockHistoryList();
		todaysHistory.setStockSymbol(stockHistoryId);
		todaysHistory.setCount(0);
		todaysHistory.setStockId(AppConstants.LATEST + stockHistoryId);
		todaysHistory.setStockList(new ArrayList<StockHistory>());
		return todaysHistory;
	}

	/* (non-Javadoc)
	 * @see com.gl.feign.stock.StockServiceProxy#getWeeksHistory(java.lang.String)
	 */
	@Override
	public StockHistoryList getWeeksHistory(String stockHistoryId) {
		printIfNotFound();
		log.error(STOCK_SERVICE_ERROR);
		StockHistoryList weeksHistoryEntity = new StockHistoryList();
		weeksHistoryEntity.setStockSymbol(stockHistoryId);
		weeksHistoryEntity.setCount(0);
		weeksHistoryEntity.setStockId(AppConstants.LATEST + stockHistoryId);
		weeksHistoryEntity.setStockList(new ArrayList<StockHistory>());
		return weeksHistoryEntity;
	}

	/**
	 * Prints the if not found.
	 */
	public void printIfNotFound() {
		if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
			log.error("StockService is not available {}", cause.getCause());
		}
	}

	/* (non-Javadoc)
	 * @see com.gl.feign.stock.StockServiceProxy#getSalableStocks()
	 */
	@Override
	public List<ca.gl.user.model.transaction.Stock> getSalableStocks() {
		log.error("StockService is not available {}", cause.getCause());
		printIfNotFound();
		return new ArrayList<>();
	}

}
