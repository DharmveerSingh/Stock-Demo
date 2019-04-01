package com.gl.model;

import lombok.Data;

/**
 * Instantiates a new user response.
 */

/**
 * Instantiates a new user response.
 */
@Data
public class UserResponse {
	
	/** The user. */
	private User user;
	
	/** The error msg. */
	private String errorMsg;
	
	/** The error. */
	private Throwable error;
}
