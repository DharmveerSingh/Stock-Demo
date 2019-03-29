package ca.gl.user.model.transaction;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonIgnore;
import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;

import lombok.Data;
import lombok.ToString;

/**
 * Instantiates a new transaction.
 */
@Data

/*
 * (non-Javadoc)
 * 
 * @see java.lang.Object#toString()
 */
@ToString
@Document
public class Transaction {

	/** The id. */
	@Id
	@Field
	/** transaction Id **/
	private String id;

	/** The user id. */
	@Field
	@NotNull
	private String userId;

	/** The stock id. */
	@Field
	@NotNull
	private String stockId;

	/** The stock name. */
	@Field
	@NotNull
	private String stockName;

	/** The stock price. */
	@Field
	private double stockPrice;

	/** The count. */
	@Field
	@NotNull
	/** Number of stocks purchased **/
	private Integer count;

	/** The bill. */
	@Field
	/** Total amount **/
	private double bill;

	/** The error msg. */
	@Field
	private String errorMsg;

	/** The status. */
	@Field
	private Status status;

	/** The error. */
	@Field
	private Error error;
	
	@Field
	private Date created=new Date();
}
