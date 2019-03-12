package ca.gl.fileUploader.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * The Class StockHistoryList.
 */
@Document

/**
 * Sets the version.
 *
 * @param version the new version
 */
@Data

/**
 * Instantiates a new stock history list.
 *
 * @param stockList the stock list
 * @param stockId the stock id
 * @param count the count
 * @param stockSymbol the stock symbol
 * @param version the version
 */
@AllArgsConstructor

/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@EqualsAndHashCode

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@ToString
public class StockHistoryList{
	
	
	
	/** The stock list. */
	@Field
	private List<StockHistory> stockList;

	/** The stock id. */
	@NonNull
	@Field
	@Id
	private String stockId;

	/** The count. */
	@Field
	private Integer count;

	/** The stock symbol. */
	@Field
	private String stockSymbol;

	/** The version. */
	@Version
	@Field
	private long version;
	
	/**
	 * Instantiates a new stock history list.
	 */
	public StockHistoryList() {
		this.count=0;
	}
}
