package ca.gl.StockExchange.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonIgnore;
import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class User {

	@Id
	@Field
	@JsonIgnore
	private String userId;
	
	@Field
	@NotNull
	private String email;
	
	@Field
	private String name;
	
	@Field 
	private String lastName;
	
	@Field
	private String password;

	@Field
	private boolean active;
	
	@Field("roles")
	List<String> roles;

}
