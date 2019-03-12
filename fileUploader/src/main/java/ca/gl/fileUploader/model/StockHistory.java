package ca.gl.fileUploader.model;

import java.time.LocalDateTime;

import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonValue;
import com.couchbase.client.java.repository.annotation.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

// TODO: Auto-generated Javadoc
/**
 * The Class StockHistory.
 */
@Document

/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@Data

/**
 * Instantiates a new stock history.
 *
 * @param closePrice the close price
 * @param PE the pe
 * @param EPS the eps
 * @param low the low
 * @param high the high
 * @param volume the volume
 * @param wkLow the wk low
 * @param wkHigh the wk high
 * @param openPrice the open price
 * @param date the date
 */
@AllArgsConstructor

/**
 * Instantiates a new stock history.
 */
@NoArgsConstructor

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@ToString
public class StockHistory implements Comparable<StockHistory>{

	/** The close price. */
	@Field
	private Double closePrice;

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

	/** The date. */
	@Field
	private LocalDateTime date;

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(StockHistory other) {
		return this.date.compareTo(other.getDate());
	}

}
