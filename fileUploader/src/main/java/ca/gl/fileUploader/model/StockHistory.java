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

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StockHistory implements Comparable<StockHistory>{

	@Field
	private Double closePrice;

	@Field
	private Double PE;

	@NonNull
	@Field
	private Double EPS;

	@NonNull
	@Field
	private Double low;

	@NonNull
	@Field
	private Double high;

	@Field
	private Long volume;

	@Field // 52-Wk Low
	private Double wkLow;

	@Field // 52-Wk High
	private Double wkHigh;

	@Field
	private Double openPrice;

	@Field
	private LocalDateTime date;

	@Override
	public int compareTo(StockHistory other) {
		return this.date.compareTo(other.getDate());
	}

}
