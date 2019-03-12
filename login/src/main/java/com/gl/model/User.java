package com.gl.model;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Version;
import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
 * Sets the version.
 *
 * @param version the new version
 */
@Data

/**
 * Instantiates a new user.
 *
 * @param id the id
 * @param email the email
 * @param name the name
 * @param lastName the last name
 * @param active the active
 * @param password the password
 * @param roles the roles
 * @param version the version
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

	/** The id. */
	@Id
	@Field("user_id")
	@JsonIgnore
	private String id;

	/** The email. */
	@Field
	@NotNull
	@Email(message = "*Please provide a valid Email")
	@NotEmpty(message = "*Please provide an email")
	private String email;

	/** The name. */
	@Field("name")
	@NotEmpty(message = "*Please provide your name")
	private String name;

	/** The last name. */
	@Field("lastName")
	private String lastName;

	/** The active. */
	@Field
	private boolean active;

	/** The password. */
	@Field
	@Length(min = 5, message = "*Your password must have at least 5 characters")
	@NotEmpty(message = "*Please provide your password")
	private String password;

	/** The roles. */
	@Field("roles")
	List<String> roles;
	
	/** The version. */
	@Version
	@Field
	private long version;

}
