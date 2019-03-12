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

// TODO: Auto-generated Javadoc
/**
 * The Class User.
 */
@Document

/**
 * Sets the roles.
 *
 * @param roles the new roles
 */
@Data

/**
 * Instantiates a new user.
 *
 * @param userId the user id
 * @param email the email
 * @param name the name
 * @param lastName the last name
 * @param password the password
 * @param active the active
 * @param roles the roles
 */
@AllArgsConstructor

/**
 * Instantiates a new user.
 */
@NoArgsConstructor

/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@EqualsAndHashCode

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@ToString
public class User {

	/** The user id. */
	@Id
	@Field
	@JsonIgnore
	private String userId;
	
	/** The email. */
	@Field
	@NotNull
	private String email;
	
	/** The name. */
	@Field
	private String name;
	
	/** The last name. */
	@Field 
	private String lastName;
	
	/** The password. */
	@Field
	private String password;

	/** The active. */
	@Field
	private boolean active;
	
	/** The roles. */
	@Field("roles")
	List<String> roles;

}
