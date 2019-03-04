package com.gl.model;

import java.text.DecimalFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ca.gl.fileUploader.model.Stock;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@JsonDeserialize
@NoArgsConstructor
public class UserStockResponse {

	// Stock
	private String StockID;
	private String stockSymbol;
	private Double prevClose;
	private Double price;
	private Double PE;
	private Double EPS;
	private Double low;
	private Double high;
	private Long volume;
	private Double wkLow;
	private Double wkHigh;
	private Double openPrice;

	// user stock
	private String userId;
	private Double lockedPrice;
	private Double currentPrice;
	private Double investment;
	private String userStockId;

	// calculation
	private String change;
	private String earning;
	private String income;
	// formater
	private DecimalFormat numberFormat = new DecimalFormat("#.00");

	public void setStock(Stock stock, UserStock userStock) {
		StockID = stock.getStockID();
		stockSymbol = stock.getStockSymbol();
		prevClose = stock.getPrevClose();
		price = stock.getPrice();
		PE = stock.getPE();
		EPS = stock.getEPS();
		low = stock.getLow();
		high = stock.getHigh();
		volume = stock.getVolume();
		wkLow = stock.getWkLow();
		wkHigh = stock.getWkHigh();
		openPrice = stock.getOpenPrice();

		userId = userStock.getUserId();
		lockedPrice = userStock.getLockedPrice();
		currentPrice = stock.getPrice();
		investment = userStock.getInvestment();
		userStockId = userStock.getUserStockId();
		double diff = price - lockedPrice;
		double c = (diff/lockedPrice)*100;
		change = numberFormat.format(c);
		double e=(c * investment) / 100;
		earning = numberFormat.format(e);
		
		income = numberFormat.format(investment+ e);
	}
}
