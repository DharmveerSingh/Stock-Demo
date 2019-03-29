package com.gl.feign.user;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.gl.model.User;
import com.gl.model.UserResponse;
import com.gl.model.UserStock;
import com.gl.model.UserStockList;

import ca.gl.user.model.transaction.Transaction;
import feign.hystrix.FallbackFactory;
import reactor.core.publisher.Flux;

/**
 * The Interface UserServiceProxy.
 */
@FeignClient(value = "urs", fallback = UserFallbackServiceProxy.class)
public interface UserServiceProxy {

	/**
	 * Signup.
	 *
	 * @param user the user
	 * @return the user response
	 */
	@PostMapping("/user/signup")
	public UserResponse signup(@RequestBody User user);

	/**
	 * Gets the user.
	 *
	 * @param userId the user id
	 * @return the user
	 */
	@GetMapping("/user/getById")
	public User getUser(@RequestParam("userId")String userId);

	/**
	 * Disable user.
	 *
	 * @param userId the user id
	 * @return the boolean
	 */
	@GetMapping("/user/disable")
	public Boolean disableUser(@RequestParam("userId")String userId);

	/**
	 * Save user stock.
	 *
	 * @param us the us
	 * @return the user stock list
	 */
	@PostMapping("/user/purchase")
	public UserStockList saveUserStock(@RequestBody UserStock us);

	/**
	 * Gets the user stock.
	 *
	 * @param username the username
	 * @return the user stock
	 */
	@GetMapping("/user/purchase")
	public UserStockList getUserStock(@RequestParam("username")String username);

	/**
	 * Removes the purchase.
	 *
	 * @param stockId the stock id
	 * @param userStockId the user stock id
	 * @return the user stock list
	 */
	@GetMapping("/user/removePurchase")
	public UserStockList removePurchase(@RequestParam("stockId")String stockId, @RequestParam("userStockId")String userStockId);

	/**
	 * Update.
	 *
	 * @param user the user
	 * @return the user response
	 */
	@PostMapping("/user/update")
	public UserResponse update(@RequestBody User user);
	
	/**
	 * Save transaction.
	 *
	 * @param transaction the transaction
	 * @return the transaction
	 */
	@PostMapping("/user/buyStocks")
	public Transaction saveTransaction(@RequestBody Transaction transaction);
	
	/**
	 * Gets the transaction.
	 *
	 * @param id the id
	 * @return the transaction
	 */
	@GetMapping("/user/transaction")
	public Transaction getTransaction(@RequestParam String id);
	
	/**
	 * Gets the transaction.
	 *
	 * @param id the id
	 * @return the transaction
	 */
	@GetMapping("/user/transactions")
	public List<Transaction> getAllTransactions(@RequestParam String userId);
	
	/**
	 * A factory for creating HystrixClientFallback objects.
	 */
	@Component
	static class HystrixClientFallbackFactory implements FallbackFactory<UserServiceProxy> {
	
	/* (non-Javadoc)
	 * @see feign.hystrix.FallbackFactory#create(java.lang.Throwable)
	 */
	@Override
	public UserServiceProxy create(Throwable cause) {
		return new UserFallbackServiceProxy(cause) ;
		}
	}
}
