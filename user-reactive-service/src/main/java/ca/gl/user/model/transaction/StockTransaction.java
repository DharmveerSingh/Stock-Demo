package ca.gl.user.model.transaction;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Version;
import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;

import lombok.Data;

/**
 * Instantiates a new stock transaction.
 */
@Data
@Document
public class StockTransaction {

	/** The id. */
	@Id
	@Field
	private String id;

	/** The stock id. */
	@Field
	private String stockId;

	/** The number of stock. */
	@Field
	private Integer numberOfStock;

	/** The created. */
	@Field
	private LocalDateTime created = LocalDateTime.now();

	/** The status. */
	@Field
	private StockStatus status;

	/** The version. */
	@Version
	@Field
	private Long version;

}
