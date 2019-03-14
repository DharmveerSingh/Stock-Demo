package com.gl.model;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * The Class UserStock.
 */
@Document

/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@Data

/**
 * Instantiates a new user stock.
 *
 * @param userStockId the user stock id
 * @param userId the user id
 * @param stockID the stock ID
 * @param stockSymbol the stock symbol
 * @param lockedPrice the locked price
 * @param currentPrice the current price
 * @param investment the investment
 */
@AllArgsConstructor

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@ToString
@JsonDeserialize
public class UserStock {

	/** The user stock id. */
	@Field
	private String userStockId;

	/** The user id. */
	@Field
	@NotEmpty
	private String userId;

	/** The stock ID. */
	@Field
	@NotEmpty
	private String stockID;

	/** The stock symbol. */
	@Field
	private String stockSymbol;

	/** The locked price. */
	@Field
	@NotEmpty
	private Double lockedPrice;

	/** The current price. */
	@Field
	private Double currentPrice;

	/** The investment. */
	@Field
	@NotEmpty
	private Double investment;
	
}
