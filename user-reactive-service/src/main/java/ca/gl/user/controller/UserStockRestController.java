package ca.gl.user.controller;

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

import ca.gl.user.model.UserStock;
import ca.gl.user.model.UserStockList;
import ca.gl.user.service.UserStockService;
import reactor.core.publisher.Mono;

/**
 * User controller.
 *
 * @author dharamveer.singh
 */
@RestController
@RequestMapping("/user")
public class UserStockRestController {

	
	/** The user stock service. */
	@Autowired
	private UserStockService userStockService;

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(UserStockRestController.class);

	/**
	 * Save user stock.
	 *
	 * @param us the us
	 * @return the mono
	 */
	@PostMapping("/purchase")
	public Mono<UserStockList> saveUserStock(@RequestBody UserStock us) {
		log.info("Inside saveUserStock");
		return userStockService.saveUserStock(us);
	}

	/**
	 * Gets the user stock.
	 *
	 * @param username the username
	 * @return the user stock
	 */
	@GetMapping("/purchase")
	public Mono<UserStockList> getUserStock(@RequestParam("username")String username) {
		log.info("Inside getUserStock");
		return userStockService.getUserStock(username);
	}

	/**
	 * Removes the purchase.
	 *
	 * @param request the request
	 * @return the mono
	 */
	@GetMapping("/removePurchase")
	public Mono<UserStockList> removePurchase(HttpServletRequest request) {
		log.info("Inside removePurchase");
		String stockId=request.getParameter("stockId");
		String userStockId=request.getParameter("userStockId");
		Mono<UserStockList> result = userStockService.removeUserStock(userStockId, stockId);
		return result;
	}
}
