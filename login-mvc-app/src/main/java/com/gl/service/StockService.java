package com.gl.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ca.gl.fileUploader.constant.AppConstants;
import ca.gl.fileUploader.model.StockHistory;
import ca.gl.fileUploader.model.StockHistoryList;

/**
 * The Class StockService.
 */
@Service
public class StockService {

	/** The se base url. */
	@Value("${stock.exchange.baseURL}")
	private String seBaseUrl;

	/** The se today history. */
	@Value("${stock.exchange.stock.today.history}")
	private String seTodayHistory;

	/** The se weeks history. */
	@Value("${stock.exchange.stock.week.history}")
	private String seWeeksHistory;
	
	/** The rest template. */
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * Get todays history by stock symbol.
	 *
	 * @param stockSymbol the stock symbol
	 * @return the todays history
	 */
	public StockHistoryList getTodaysHistory(String stockSymbol) {
		String url = seBaseUrl + seTodayHistory + AppConstants.REGULAR_HISTORY + stockSymbol;
		ResponseEntity<StockHistoryList> todaysHistoryEntity = restTemplate.getForEntity(url, StockHistoryList.class);
		StockHistoryList response;

		if (todaysHistoryEntity.hasBody())
			response = todaysHistoryEntity.getBody();
		else {
			response = new StockHistoryList();
			response.setStockSymbol(stockSymbol);
			response.setCount(0);
			response.setStockId(AppConstants.LATEST + stockSymbol);
			response.setStockList(new ArrayList<StockHistory>());
		}
		return response;
	}

	/**
	 * Get weeks history for stockSymbol.
	 *
	 * @param stockSymbol the stock symbol
	 * @return the weeks history
	 */
	public StockHistoryList getWeeksHistory(String stockSymbol) {
		String url = seBaseUrl + seWeeksHistory + AppConstants.REGULAR_HISTORY + stockSymbol;
		ResponseEntity<StockHistoryList> weeksHistoryEntity = restTemplate.getForEntity(url, StockHistoryList.class);
		StockHistoryList response;

		if (weeksHistoryEntity.hasBody())
			response = weeksHistoryEntity.getBody();
		else {
			response = new StockHistoryList();
			response.setStockSymbol(stockSymbol);
			response.setCount(0);
			response.setStockId(AppConstants.LATEST + stockSymbol);
			response.setStockList(new ArrayList<StockHistory>());
		}
		return response;
	}

}
