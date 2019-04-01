package com.gl.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.feign.stock.StockServiceProxy;

import ca.gl.fus.constant.AppConstants;
import ca.gl.fus.model.StockHistory;
import ca.gl.fus.model.StockHistoryList;

/**
 * The Class StockService.
 */
@Service
public class StockService {
		
	/** The stock service proxy. */
	@Autowired
	private StockServiceProxy stockServiceProxy;
	
	/**
	 * Get todays history by stock symbol.
	 *
	 * @param stockSymbol the stock symbol
	 * @return the todays history
	 */
	public StockHistoryList getTodaysHistory(String stockSymbol) {
		StockHistoryList todaysHistory=stockServiceProxy.getTodaysHistory(AppConstants.REGULAR_HISTORY+stockSymbol);
		
		if(todaysHistory== null) {
			todaysHistory = new StockHistoryList();
			todaysHistory.setStockSymbol(stockSymbol);
			todaysHistory.setCount(0);
			todaysHistory.setStockId(AppConstants.LATEST + stockSymbol);
			todaysHistory.setStockList(new ArrayList<StockHistory>());
		}
		return todaysHistory;
	}

	/**
	 * Get weeks history for stockSymbol.
	 *
	 * @param stockSymbol the stock symbol
	 * @return the weeks history
	 */
	public StockHistoryList getWeeksHistory(String stockSymbol) {
		StockHistoryList weeksHistoryEntity = stockServiceProxy.getWeeksHistory(AppConstants.REGULAR_HISTORY + stockSymbol);

		if (weeksHistoryEntity== null) {
			weeksHistoryEntity = new StockHistoryList();
			weeksHistoryEntity.setStockSymbol(stockSymbol);
			weeksHistoryEntity.setCount(0);
			weeksHistoryEntity.setStockId(AppConstants.LATEST + stockSymbol);
			weeksHistoryEntity.setStockList(new ArrayList<StockHistory>());
		}
		return weeksHistoryEntity;
	}

}
