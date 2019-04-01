package ca.gl.srs.responses;

import ca.gl.srs.model.User;
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
}
