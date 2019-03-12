package com.gl.model;

import java.text.DecimalFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ca.gl.fileUploader.model.Stock;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@Data

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@ToString
@JsonDeserialize

/**
 * Instantiates a new user stock response.
 */
@NoArgsConstructor
public class UserStockResponse {

	/** The Stock ID. */
	// Stock
	private String StockID;
	
	/** The stock symbol. */
	private String stockSymbol;
	
	/** The prev close. */
	private Double prevClose;
	
	/** The price. */
	private Double price;
	
	/** The pe. */
	private Double PE;
	
	/** The eps. */
	private Double EPS;
	
	/** The low. */
	private Double low;
	
	/** The high. */
	private Double high;
	
	/** The volume. */
	private Long volume;
	
	/** The wk low. */
	private Double wkLow;
	
	/** The wk high. */
	private Double wkHigh;
	
	/** The open price. */
	private Double openPrice;

	/** The user id. */
	// user stock
	private String userId;
	
	/** The locked price. */
	private Double lockedPrice;
	
	/** The current price. */
	private Double currentPrice;
	
	/** The investment. */
	private Double investment;
	
	/** The user stock id. */
	private String userStockId;

	/** The change. */
	// calculation
	private String change;
	
	/** The earning. */
	private String earning;
	
	/** The income. */
	private String income;
	
	/** The number format. */
	// formater
	private DecimalFormat numberFormat = new DecimalFormat("#.00");

	/**
	 * Sets the stock.
	 *
	 * @param stock the stock
	 * @param userStock the user stock
	 */
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
