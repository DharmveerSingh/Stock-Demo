package ca.gl.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.gl.user.model.User;
import ca.gl.user.model.transaction.Transaction;
import ca.gl.user.responses.UserResponse;
import ca.gl.user.service.UserService;
import reactor.core.publisher.Flux;
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
	 * @param user the user
	 * @return the mono
	 */
	@PostMapping("/signup")
	public Mono<UserResponse> signup(@RequestBody User user) {
		log.info("Inside signup");
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
	 * Signup.
	 *
	 * @param user the user
	 * @return the mono
	 */
	@PostMapping("/update")
	public Mono<UserResponse> update(@RequestBody User user) {
		log.info("Inside update");
		Mono<UserResponse> response = userService.validateUser(user);
		return response.map(res -> {
			if (res.getErrorMsg() != null) {
				 return res;
			} else {
				userService.update(user).subscribe(ur -> res.setUser(ur));
				return res;
			}
		});
	}

	/**
	 * Gets the user.
	 *
	 * @param userId the user id
	 * @return the user
	 */
	@GetMapping("/getById")
	public Mono<User> getUser(@RequestParam("userId")String userId){
		log.info("Inside getUser");
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
		log.info("Inside disableUser");
		return userService.disableUser(userId);
	}
	
	/**
	 * Save transaction.
	 *
	 * @param transaction the transaction
	 * @return the mono
	 */
	@PostMapping("/buyStocks")
	public Mono<Transaction> saveTransaction(@RequestBody Transaction transaction) {
		log.info("Inside saveTransaction");
		return  userService.saveTransaction(transaction);
	}
	
	/**
	 * Gets the transaction.
	 *
	 * @param id the id
	 * @return the transaction
	 */
	@GetMapping("/transaction")
	public Mono<Transaction> getTransaction(@RequestParam String id) {
		log.info("Inside getTransaction with id: {}",id);
		return userService.getTransaction(id);
	}
	
	@GetMapping("/transactions")
	public Flux<Transaction> getAllTransactions(@RequestParam String userId){
		log.info("Inside getAllTransactions with userId: {}",userId);
		return userService.getAllTransaction(userId);
	}
}
