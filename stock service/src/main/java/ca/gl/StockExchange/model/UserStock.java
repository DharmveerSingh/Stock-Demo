package ca.gl.StockExchange.model;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Document
@Data
@AllArgsConstructor
@ToString
@JsonDeserialize 
public class UserStock {
	 
	@Field
	private String userStockId;
	
	@Field
	@NotEmpty
	private String userId;

	@Field
	@NotEmpty
	private String stockID;

	@Field
	private String stockSymbol;
	
	@Field
	@NotEmpty
	private Double lockedPrice;
	
	@Field
	private Double currentPrice;
	
	@Field
	@NotEmpty
	private Double investment;
}
