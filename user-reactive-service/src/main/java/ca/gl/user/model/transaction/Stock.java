package ca.gl.user.model.transaction;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;

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

@Data

/**
 * Instantiates a new stock.
 *
 * @param id          the id
 * @param stockSymbol the stock symbol
 * @param unitPrice   the unit price
 * @param totalCount  the total count
 * @param created     the created
 * @param modified    the modified
 * @param version     the version
 */
@AllArgsConstructor

/**
 * Instantiates a new stock.
 */
@NoArgsConstructor

/*
 * (non-Javadoc)
 * 
 * @see java.lang.Object#hashCode()
 */
@EqualsAndHashCode

/*
 * (non-Javadoc)
 * 
 * @see java.lang.Object#toString()
 */

@ToString
public class Stock implements Serializable, Comparable<Stock> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1456254210366306630L;

	/**
	 * The Stock ID. Id will go like Stock::UNIT::STOCKSYMBOL
	 */
	@NonNull
	@Field
	@Id
	private String id;

	/** The stock symbol. */
	@NonNull
	@Field
	private String stockSymbol;

	/** The price. */
	@Field
	private Double unitPrice;

	/** The total count. */
	@Field
	private long totalCount;

	/** The created. */
	@Field
	private LocalDateTime created;

	/** The modified. */
	@Field
	private LocalDateTime modified;

	/** The version. */
	@Field
	@Version
	private long version;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Stock o) {
		return this.stockSymbol.compareTo(o.getStockSymbol());
	}
}