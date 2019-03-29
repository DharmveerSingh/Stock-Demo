package ca.gl.user.service;

import java.time.LocalTime;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.gl.user.constant.AppConstants;
import ca.gl.user.dao.TransactionRepository;
import ca.gl.user.dao.UserRepository;
import ca.gl.user.kafka.producer.MessageProducer;
import ca.gl.user.model.User;
import ca.gl.user.model.transaction.Transaction;
import ca.gl.user.responses.UserResponse;
import ca.gl.user.utility.Utils;
import reactor.core.publisher.Flux;
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

	/** The transaction repo. */
	@Autowired
	private TransactionRepository transactionRepo;

	/** The kafka. */
	@Autowired
	private MessageProducer kafka;

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
	 * @param user    the user
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

	/**
	 * Update.
	 *
	 * @param user the user
	 * @return the mono<? extends user>
	 */
	public Mono<? extends User> update(User user) {
		user.setUserId(AppConstants.USER_PREFIX + user.getEmail());
		return userRepo.save(user);
	}

	/**
	 * Save transaction.
	 *
	 * @param transaction the transaction
	 * @return the mono
	 */
	public Mono<Transaction> saveTransaction(Transaction transaction) {
		String transactionId = genrateTransactionid(transaction);
		log.info("Saving transaction with ID: {}", transactionId);
		transaction.setId(transactionId);
		return saveAndSendEvent(transaction);

	}

	/**
	 * Save and send event.
	 *
	 * @param transaction the transaction
	 * @return the mono
	 */
	private Mono<Transaction> saveAndSendEvent(Transaction transaction) {
		return transactionRepo.save(transaction).map(tr -> {
			log.error("Sending event BuyStocks to kafka");
			kafka.sentBuyStocks(transaction);
			return tr;
		});
	}

	/**
	 * Genrate transactionid.
	 *
	 * @param transaction the transaction
	 * @return the string
	 */
	private String genrateTransactionid(Transaction transaction) {
		StringBuilder transactionIdBuilder = new StringBuilder(AppConstants.TRANSACTION_PREFIX);
		transactionIdBuilder.append(transaction.getUserId()).append("::").append(transaction.getStockId()).append("::")
				.append(LocalTime.now());
		return transactionIdBuilder.toString();
	}

	/**
	 * Gets the transaction.
	 *
	 * @param id the id
	 * @return the transaction
	 */
	public Mono<Transaction> getTransaction(String id) {
		return transactionRepo.findById(id);
	}

	public Flux<Transaction> getAllTransaction(String userId) {
		String queryParam=AppConstants.TRANSACTION_PREFIX+userId+"%";
		return transactionRepo.findAllByUserId(queryParam);
	}

}
