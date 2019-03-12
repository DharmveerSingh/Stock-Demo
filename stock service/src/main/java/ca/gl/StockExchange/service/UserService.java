package ca.gl.StockExchange.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.gl.StockExchange.constant.AppConstants;
import ca.gl.StockExchange.dao.UserRepository;
import ca.gl.StockExchange.dao.UserStockListRepository;
import ca.gl.StockExchange.model.User;
import ca.gl.StockExchange.model.UserStock;
import ca.gl.StockExchange.model.UserStockList;
import ca.gl.StockExchange.responses.UserResponse;
import ca.gl.StockExchange.utility.Utils;
import reactor.core.publisher.Mono;

/**
 * The Class UserService.
 */
@Service
public class UserService {
	
	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	/** The user repo. */
	@Autowired
	private UserRepository userRepo;

	/** The us stock list repo. */
	@Autowired
	private UserStockListRepository usStockListRepo;

	/**
	 * User signup.
	 *
	 * @param user the user
	 * @return the mono
	 */
	public Mono<UserResponse> signUp(User user) {
		return userRepo.existsById(AppConstants.USER_PREFIX + user.getEmail())
				.flatMap(isFound -> createIfNotExist(user, isFound));

	}

	/**
	 * Create user if not exist.
	 *
	 * @param user the user
	 * @param isFound the is found
	 * @return the mono
	 */
	private Mono<UserResponse> createIfNotExist(User user, boolean isFound) {
		final String username = AppConstants.USER_PREFIX + user.getEmail();
		Mono<UserResponse> userResult = null;
		UserResponse res = new UserResponse();
		if (isFound) {
			log.error("User already exist with ID: {} ", username);
			res.setErrorMsg(AppConstants.ERROR_USER_EXIST);

		} else {
			log.info("Saving user with ID: {} ", username);
			user.setUserId(username);

			userResult = userRepo.save(user).map(u -> {
				log.info("User saved. removing password field from response");
				user.setPassword(AppConstants.BLANK_STRING);
				res.setUser(user);
				return res;
			}).doOnError(err -> res.setErrorMsg(err.getMessage()));
			;
		}

		if (res.getErrorMsg() != null) {
			return Mono.just(res);
		} else
			return userResult;
	}

	/**
	 * call Validate user.
	 *
	 * @param user the user
	 * @return the mono
	 */
	public Mono<UserResponse> validateUser(User user) {
		return Mono.just(validate(user));
	}

	/**
	 * Validae user.
	 *
	 * @param user the user
	 * @return the user response
	 */
	private UserResponse validate(User user) {
		UserResponse res = new UserResponse();
		if (Utils.emptyString(user.getEmail())) {
			res.setErrorMsg(AppConstants.BLANK_EMAIL_ERROR);
		} else if (Utils.emptyString(user.getPassword())) {
			res.setErrorMsg(AppConstants.BLANK_PASSWORD_ERROR);
		}
		return res;
	}

	/**
	 * Save user stocks.
	 *
	 * @param us the us
	 * @return the mono
	 */
	public Mono<UserStockList> saveUserStock(UserStock us) {
		return usStockListRepo.findById(AppConstants.PURCHASE_PREFIX + us.getUserId())
				.flatMap(usl -> updateUSL(us, usl)).switchIfEmpty(createUserStockList(us));
	}

	/**
	 * Update User stock list.
	 *
	 * @param us the us
	 * @param usl the usl
	 * @return the mono
	 */
	private Mono<UserStockList> updateUSL(UserStock us, UserStockList usl) {
		if (usl != null) {
			log.info("Update User stock list with id: " + usl.getUserStockId());
			usl.getUserStocks().add(us);
			usl.setCount(usl.getCount() + 1);
			return usStockListRepo.save(usl);
		}

		return Mono.empty();
	}

	/**
	 * Create user stock list.
	 *
	 * @param us the us
	 * @return the mono<? extends user stock list>
	 */
	private Mono<? extends UserStockList> createUserStockList(UserStock us) {
		List<UserStock> list = new ArrayList<UserStock>();
		list.add(us);
		UserStockList usl = new UserStockList(AppConstants.PURCHASE_PREFIX + us.getUserId(), list, list.size());
		return usStockListRepo.save(usl);
	}

	/**
	 * Get user stock list.
	 *
	 * @param username the username
	 * @return the user stock
	 */
	public Mono<UserStockList> getUserStock(String username) {
		return usStockListRepo.findById(AppConstants.PURCHASE_PREFIX + username);
	}

	/**
	 * Remove user stock list.
	 *
	 * @param userStockId the user stock id
	 * @param stockId the stock id
	 * @return the mono
	 */
	public Mono<UserStockList> removeUserStock(String userStockId, String stockId) {
		return usStockListRepo.findById(userStockId).flatMap(usStock -> removeStock(stockId, usStock));

	}

	/**
	 * Remove stock from user's list.
	 *
	 * @param stockId the stock id
	 * @param usStock the us stock
	 * @return the mono
	 */
	private Mono<UserStockList> removeStock(String stockId, UserStockList usStock) {
		List<UserStock> stockList = usStock.getUserStocks().stream()
				.filter(stock -> !stock.getUserStockId().equals(stockId)).collect(Collectors.toList());
		usStock.setUserStocks(stockList);
		usStock.setCount(stockList.size());
		return usStockListRepo.save(usStock);
	}

	/**
	 * Find user.
	 *
	 * @param userId the user id
	 * @return the user
	 */
	public Mono<User> getUser(String userId) {
		return userRepo.findById(userId);
	}

	/**
	 * Disable user.
	 *
	 * @param userId the user id
	 * @return the mono
	 */
	public Mono<Boolean> disableUser(String userId) {
		return userRepo.findById(userId).flatMap(updateUserActiveStatus()).switchIfEmpty(Mono.just(Boolean.FALSE));
	}

	/**
	 * Update user active status to false.
	 *
	 * @return the function<? super user,? extends mono<? extends boolean>>
	 */
	private Function<? super User, ? extends Mono<? extends Boolean>> updateUserActiveStatus() {
		return user -> {
			user.setActive(Boolean.FALSE);
			return userRepo.save(user).flatMap(u -> Mono.just(Boolean.TRUE)).switchIfEmpty(Mono.just(Boolean.FALSE));
		};
	}

}
