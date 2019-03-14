package ca.gl.StockExchange.responses;

import ca.gl.StockExchange.model.User;
import lombok.Data;

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
