package com.gl.feign.user;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import com.couchbase.client.core.ServiceNotAvailableException;
import com.gl.model.User;
import com.gl.model.UserResponse;
import com.gl.model.UserStock;
import com.gl.model.UserStockList;

import ca.gl.user.model.transaction.Error;
import ca.gl.user.model.transaction.Status;
import ca.gl.user.model.transaction.Transaction;
import feign.FeignException;
import feign.hystrix.FallbackFactory;

/**
 * The Class UserFallbackServiceProxy.
 */
@Component
public class UserFallbackServiceProxy implements UserServiceProxy, FallbackFactory<UserServiceProxy> {

	/** The Constant USER_SERVICE_NOT_AVAILABLE. */
	private static final String USER_SERVICE_NOT_AVAILABLE = "UserService not available";

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(UserFallbackServiceProxy.class);

	/** The cause. */
	final private Throwable cause;

	/**
	 * Instantiates a new user fallback service proxy.
	 *
	 * @param cause the cause
	 */
	public UserFallbackServiceProxy(Throwable cause) {
		this.cause = cause;
	}

	/* (non-Javadoc)
	 * @see com.gl.feign.user.UserServiceProxy#signup(com.gl.model.User)
	 */
	@Override
	public UserResponse signup(User user) {
		printIfNotFound();
		log.error("signup: service not found falling back to userFallbackservice");
		UserResponse ur = new UserResponse();
		ur.setErrorMsg(USER_SERVICE_NOT_AVAILABLE);
		ur.setError(new ServiceNotAvailableException(USER_SERVICE_NOT_AVAILABLE));
		return ur;
	}

	/* (non-Javadoc)
	 * @see com.gl.feign.user.UserServiceProxy#getUser(java.lang.String)
	 */
	@Override
	public User getUser(String userId) {
		printIfNotFound();
		log.error("getUser: service not found falling back to userFallbackservice");
		User u = new User();
		u.setError(new ServiceNotAvailableException(USER_SERVICE_NOT_AVAILABLE));
		return u;
	}

	/* (non-Javadoc)
	 * @see com.gl.feign.user.UserServiceProxy#disableUser(java.lang.String)
	 */
	@Override
	public Boolean disableUser(String userId) {
		printIfNotFound();
		log.error("disableUser: service not found falling back to userFallbackservice");
		return false;
	}

	/* (non-Javadoc)
	 * @see com.gl.feign.user.UserServiceProxy#saveUserStock(com.gl.model.UserStock)
	 */
	@Override
	public UserStockList saveUserStock(UserStock us) {
		printIfNotFound();
		log.error("saveUserStock: service not found falling back to userFallbackservice");
		UserStockList usl = new UserStockList();
		usl.setError(new ServiceNotAvailableException(USER_SERVICE_NOT_AVAILABLE));

		return usl;
	}

	/* (non-Javadoc)
	 * @see com.gl.feign.user.UserServiceProxy#getUserStock(java.lang.String)
	 */
	@Override
	public UserStockList getUserStock(String username) {
		printIfNotFound();
		log.error("getUserStock: service not found falling back to userFallbackservice");
		UserStockList usl = new UserStockList();
		usl.setError(new ServiceNotAvailableException(USER_SERVICE_NOT_AVAILABLE));
		return usl;
	}

	/* (non-Javadoc)
	 * @see com.gl.feign.user.UserServiceProxy#removePurchase(java.lang.String, java.lang.String)
	 */
	@Override
	public UserStockList removePurchase(@RequestParam("stockId") String stockId,
			@RequestParam("userStockId") String userStockId) {
		printIfNotFound();
		log.error("removePurchase: service not found falling back to userFallbackservice");
		UserStockList usl = new UserStockList();
		usl.setError(new ServiceNotAvailableException(USER_SERVICE_NOT_AVAILABLE));
		return usl;
	}

	/* (non-Javadoc)
	 * @see com.gl.feign.user.UserServiceProxy#update(com.gl.model.User)
	 */
	@Override
	public UserResponse update(User user) {
		printIfNotFound();
		log.error("removePurchase: service not found falling back to userFallbackservice");
		return new UserResponse();
	}

	/**
	 * Prints the if not found.
	 */
	public void printIfNotFound() {
		if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
			log.error("UserService is not available {}", cause.getCause());
		}
	}

	/* (non-Javadoc)
	 * @see feign.hystrix.FallbackFactory#create(java.lang.Throwable)
	 */
	@Override
	public UserServiceProxy create(Throwable cause) {
		log.error(cause.getLocalizedMessage());
		return new UserFallbackServiceProxy(cause);
	}

	/* (non-Javadoc)
	 * @see com.gl.feign.user.UserServiceProxy#saveTransaction(ca.gl.user.model.transaction.Transaction)
	 */
	@Override
	public Transaction saveTransaction(Transaction transaction) {
		log.error("saveTransaction: service not found falling back to userFallbackservice");
		transaction.setError(Error.NOT_FOUND);
		transaction.setErrorMsg("User Service is not available");
		transaction.setStatus(Status.ERROR);
		return transaction;
	}

	/* (non-Javadoc)
	 * @see com.gl.feign.user.UserServiceProxy#getTransaction(java.lang.String)
	 */
	@Override
	public Transaction getTransaction(String id) {
		log.error("getTransaction: service not found falling back to userFallbackservice");
		Transaction transaction = new Transaction();
		transaction.setError(Error.NOT_FOUND);
		transaction.setErrorMsg("User Service is not available");
		transaction.setStatus(Status.ERROR);
		return transaction;
	}

	@Override
	public List<Transaction> getAllTransactions(String userId) {
		log.error("getAllTransactions: service not found falling back to userFallbackservice");
		return new ArrayList<>();
	}
}
