package ca.gl.StockExchange.responses;

import ca.gl.StockExchange.model.User;
import lombok.Data;

@Data
public class UserResponse {
	private User user;
	private String errorMsg;
}
