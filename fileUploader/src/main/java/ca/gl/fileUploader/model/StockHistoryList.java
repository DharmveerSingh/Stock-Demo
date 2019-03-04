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

@Document
@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class StockHistoryList{
	
	
	
	@Field
	private List<StockHistory> stockList;

	@NonNull
	@Field
	@Id
	private String stockId;

	@Field
	private Integer count;

	@Field
	private String stockSymbol;

	@Version
	@Field
	private long version;
	
	public StockHistoryList() {
		this.count=0;
	}
}
