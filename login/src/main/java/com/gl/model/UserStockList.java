package com.gl.model;

import java.util.List;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonIgnore;
import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

// TODO: Auto-generated Javadoc
/**
 * The Class UserStockList.
 */
@Document

/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@Data

/**
 * Instantiates a new user stock list.
 *
 * @param userStockId the user stock id
 * @param userStocks the user stocks
 * @param count the count
 */
@AllArgsConstructor

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@ToString
@JsonDeserialize 
public class UserStockList {
	
	/** The user stock id. */
	@Id
	@Field
	@JsonIgnore
	private String userStockId;
	
	/** The user stocks. */
	@Field
	List<UserStock> userStocks;
	
	/** The count. */
	@Field
	private Integer count;
	
}
