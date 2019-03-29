package com.gl.model;

import javax.servlet.http.HttpServletRequest;

import lombok.Data;

/**
 * Instantiates a new user signup request.
 */
@Data
public class UserSignupRequest {

	/** The request. */
	private HttpServletRequest request;
	
	/** The user. */
	private User user;
}
