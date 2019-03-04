package com.gl.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ca.gl.fileUploader.model.StockHistory;
import ca.gl.fileUploader.model.StockHistoryList;
import constant.AppConstants;

@Service
public class StockService {
	
	@Value("${stock.exchange.baseURL}")
	private String seBaseUrl;
	
	@Value("${stock.exchange.stock.today.history}")
	private String seTodayHistory;
	
	@Value("${stock.exchange.stock.week.history}")
	private String seWeeksHistory;
	@Autowired
	private RestTemplate restTemplate;
	
	public StockHistoryList getTodaysHistory(String stockSymbol) {
		String url = seBaseUrl + seTodayHistory + AppConstants.REGULAR_HISTORY+stockSymbol;
		ResponseEntity<StockHistoryList> todaysHistoryEntity=restTemplate.getForEntity(url, StockHistoryList.class);
		StockHistoryList response;
		
		if(todaysHistoryEntity.hasBody())
			response= todaysHistoryEntity.getBody();
		else {
			response=new StockHistoryList();
			response.setStockSymbol(stockSymbol);
			response.setCount(0);
			response.setStockId(AppConstants.LATEST+stockSymbol);
			response.setStockList(new ArrayList<StockHistory>());
		}
		return response;
	}

	public StockHistoryList getWeeksHistory(String stockSymbol) {
		String url = seBaseUrl + seWeeksHistory + AppConstants.REGULAR_HISTORY+stockSymbol;
		ResponseEntity<StockHistoryList> weeksHistoryEntity=restTemplate.getForEntity(url, StockHistoryList.class);
		StockHistoryList response;
		
		if(weeksHistoryEntity.hasBody())
			response= weeksHistoryEntity.getBody();
		else {
			response=new StockHistoryList();
			response.setStockSymbol(stockSymbol);
			response.setCount(0);
			response.setStockId(AppConstants.LATEST+stockSymbol);
			response.setStockList(new ArrayList<StockHistory>());
		}
		return response;
	}

}
