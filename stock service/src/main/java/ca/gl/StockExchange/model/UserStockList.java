package ca.gl.StockExchange.model;

import java.util.List;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonIgnore;
import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Document
@Data
@AllArgsConstructor
@ToString
@JsonDeserialize 
public class UserStockList {

	
	@Id
	@Field
	@JsonIgnore
	private String userStockId;
	
	@Field
	List<UserStock> userStocks;
	
	@Field
	private Integer count;
}
