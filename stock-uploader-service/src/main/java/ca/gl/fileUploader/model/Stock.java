package ca.gl.fileUploader.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;

import ca.gl.fileUploader.constant.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * The Class Stock.
 */
@Document

/**
 * Sets the open price.
 *
 * @param openPrice the new open price
 */
@Data

/**
 * Instantiates a new stock.
 *
 * @param StockID the stock ID
 * @param stockSymbol the stock symbol
 * @param prevClose the prev close
 * @param price the price
 * @param PE the pe
 * @param EPS the eps
 * @param low the low
 * @param high the high
 * @param volume the volume
 * @param wkLow the wk low
 * @param wkHigh the wk high
 * @param openPrice the open price
 */
@AllArgsConstructor

/**
 * Instantiates a new stock.
 */
@NoArgsConstructor

/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@EqualsAndHashCode

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@ToString
public class Stock implements Serializable, Comparable<Stock> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1456254210366306630L;

	/** The Stock ID. */
	@NonNull
	@Field
	@Id
	private String StockID;

	/** The stock symbol. */
	@NonNull
	@Field
	private String stockSymbol;

	/** The prev close. */
	@Field
	private Double prevClose;

	/** The price. */
	@Field
	private Double price;

	/** The pe. */
	@Field
	private Double PE;

	/** The eps. */
	@NonNull
	@Field
	private Double EPS;

	/** The low. */
	@NonNull
	@Field
	private Double low;

	/** The high. */
	@NonNull
	@Field
	private Double high;

	/** The volume. */
	@Field
	private Long volume;

	/** The wk low. */
	@Field // 52-Wk Low
	private Double wkLow;

	/** The wk high. */
	@Field // 52-Wk High
	private Double wkHigh;

	/** The open price. */
	@Field
	private Double openPrice;

	/**
	 * To history list.
	 *
	 * @return the stock history list
	 */
	public StockHistoryList toHistoryList() {
		StockHistoryList list = new StockHistoryList();
		StockHistory stockHistory = new StockHistory();
		list.setStockId(AppConstants.DAILY_HISTORY + this.stockSymbol);
		stockHistory.setClosePrice(this.price);
		stockHistory.setPE(this.PE);
		stockHistory.setEPS(this.EPS);
		stockHistory.setLow(this.low);
		stockHistory.setHigh(this.high);
		stockHistory.setVolume(this.volume);
		stockHistory.setWkLow(this.wkLow);
		stockHistory.setWkHigh(this.wkHigh);
		stockHistory.setOpenPrice(this.openPrice);
		stockHistory.setDate(LocalDateTime.now());
		list.setStockList(new ArrayList<StockHistory>());
		list.getStockList().add(stockHistory);
		list.setCount(1);
		list.setStockSymbol(this.stockSymbol);
		return list;
	}

	/**
	 * To regular history list.
	 *
	 * @return the stock history list
	 */
	public StockHistoryList toRegularHistoryList() {
		StockHistoryList list = new StockHistoryList();
		StockHistory stockHistory = new StockHistory();
		list.setStockId(AppConstants.REGULAR_HISTORY + this.stockSymbol);
		stockHistory.setClosePrice(this.price);
		stockHistory.setPE(this.PE);
		stockHistory.setEPS(this.EPS);
		stockHistory.setLow(this.low);
		stockHistory.setHigh(this.high);
		stockHistory.setVolume(this.volume);
		stockHistory.setWkLow(this.wkLow);
		stockHistory.setWkHigh(this.wkHigh);
		stockHistory.setOpenPrice(this.openPrice);
		stockHistory.setDate(LocalDateTime.now());
		list.setStockList(new ArrayList<StockHistory>());
		list.getStockList().add(stockHistory);
		list.setCount(1);
		list.setStockSymbol(this.stockSymbol);
		System.out.println("Called toRegularHostoryList for: "+ this.getStockSymbol());
		return list;
	}
	
	/**
	 * To history.
	 *
	 * @return the stock history
	 */
	public StockHistory toHistory() {
		StockHistory stockHistory = new StockHistory();
		stockHistory.setClosePrice(this.price);
		stockHistory.setPE(this.PE);
		stockHistory.setEPS(this.EPS);
		stockHistory.setLow(this.low);
		stockHistory.setHigh(this.high);
		stockHistory.setVolume(this.volume);
		stockHistory.setWkLow(this.wkLow);
		stockHistory.setWkHigh(this.wkHigh);
		stockHistory.setOpenPrice(this.openPrice);
		stockHistory.setDate(LocalDateTime.now());
		return stockHistory;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Stock o) {
		return this.stockSymbol.compareTo(o.getStockSymbol());
	}
}