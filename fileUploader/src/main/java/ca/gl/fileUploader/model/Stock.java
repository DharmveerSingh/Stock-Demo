package ca.gl.fileUploader.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;

import constant.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Stock implements Serializable, Comparable<Stock> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1456254210366306630L;

	@NonNull
	@Field
	@Id
	private String StockID;

	@NonNull
	@Field
	private String stockSymbol;

	@Field
	private Double prevClose;

	@Field
	private Double price;

	@Field
	private Double PE;

	@NonNull
	@Field
	private Double EPS;

	@NonNull
	@Field
	private Double low;

	@NonNull
	@Field
	private Double high;

	@Field
	private Long volume;

	@Field // 52-Wk Low
	private Double wkLow;

	@Field // 52-Wk High
	private Double wkHigh;

	@Field
	private Double openPrice;

	@Version
	@Field
	private long version;
	
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

	@Override
	public int compareTo(Stock o) {
		return this.stockSymbol.compareTo(o.getStockSymbol());
	}
}