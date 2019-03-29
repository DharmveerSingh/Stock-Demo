package ca.gl.user.model.transaction;

import java.util.List;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;

import lombok.Data;

/**
 * Instantiates a new transaction history.
 */
@Data
@Document
public class TransactionHistory {

	/** The transactions. */
	@Field
	private List<Transaction> transactions;

	/** The id. */
	@Field
	@Id
	private String id;
}
