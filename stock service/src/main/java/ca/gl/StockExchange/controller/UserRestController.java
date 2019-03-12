package ca.gl.StockExchange.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.gl.StockExchange.model.User;
import ca.gl.StockExchange.model.UserStock;
import ca.gl.StockExchange.model.UserStockList;
import ca.gl.StockExchange.responses.UserResponse;
import ca.gl.StockExchange.service.UserService;
import reactor.core.publisher.Mono;

/**
 * User controller.
 *
 * @author dharamveer.singh
 */
@RestController
@RequestMapping("/user")
public class UserRestController {

	/** The user service. */
	@Autowired
	private UserService userService;

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(UserRestController.class);

	/**
	 * Signup.
	 *
	 * @param request the request
	 * @param user the user
	 * @return the mono
	 */
	@PostMapping("/signup")
	public Mono<UserResponse> signup(HttpServletRequest request, @RequestBody User user) {
		Mono<UserResponse> response = userService.validateUser(user);
		return response.flatMap(res -> {
			if (res.getErrorMsg() != null) {
				return Mono.just(res);
			} else {
				return userService.signUp(user);
			}
		});
	}

	/**
	 * Save user stock.
	 *
	 * @param us the us
	 * @return the mono
	 */
	@PostMapping("/purchase")
	public Mono<UserStockList> saveUserStock(@RequestBody UserStock us) {
		return userService.saveUserStock(us);
	}

	/**
	 * Gets the user stock.
	 *
	 * @param req the req
	 * @return the user stock
	 */
	@GetMapping("/purchase")
	public Mono<UserStockList> getUserStock(HttpServletRequest req) {
		return userService.getUserStock(req.getParameter("user"));
	}

	/**
	 * Removes the purchase.
	 *
	 * @param userStockId the user stock id
	 * @param stockId the stock id
	 * @return the mono
	 */
	@GetMapping("/removePurchase")
	public Mono<UserStockList> removePurchase(@RequestParam("userStockId") String userStockId,
			@RequestParam("stockId") String stockId) {
		System.out.println("Got your request.. keep kalm: " + userStockId + ", stockId: " + stockId);
		Mono<UserStockList> result = userService.removeUserStock(userStockId, stockId);
		// log.info("Removing purchase: "+ urlBuilder.toString());
		return result;
	}
	
	/**
	 * Gets the user.
	 *
	 * @param userId the user id
	 * @return the user
	 */
	@GetMapping("/getById")
	public Mono<User> getUser(@RequestParam("userId")String userId){
		
		return userService.getUser(userId);
	}
	
	/**
	 * Disable user.
	 *
	 * @param userId the user id
	 * @return the mono
	 */
	@GetMapping("/disable")
	public Mono<Boolean> disableUser(@RequestParam("userId")String userId){
		return userService.disableUser(userId);
	}
	
}
