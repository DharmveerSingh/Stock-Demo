package ca.gl.user.responses;

import ca.gl.user.model.User;
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
