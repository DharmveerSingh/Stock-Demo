package ca.gl.user.kafka.listener;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import ca.gl.user.dao.TransactionRepository;
import ca.gl.user.dao.UserRepository;
import ca.gl.user.kafka.producer.MessageProducer;
import ca.gl.user.model.User;
import ca.gl.user.model.transaction.Status;
import ca.gl.user.model.transaction.Transaction;
import reactor.core.publisher.Mono;

/**
 * Listen kafka message.
 *
 * @author dharamveer.singh
 */
@Component
public class KafkaResultListener {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(KafkaResultListener.class);

	/** The tran repo. */
	@Autowired
	private TransactionRepository tranRepo;

	/** The user repo. */
	@Autowired
	private UserRepository userRepo;

	/** The kafka. */
	@Autowired
	private MessageProducer kafka;

	/**
	 * Listen group buyResult.
	 *
	 * @param transaction the stock
	 */
	@KafkaListener(topics = "${message.topic.name.buy.result}", groupId = "buyResult", containerFactory = "buyResultKafkaListenerContainerFactory")
	public void listenGroupBuyResult(Transaction transaction) {

		log.error("Recieved Kafka buy result as: {}", transaction);
		if (transaction.getError() == null) {
			if (transaction.getStatus() == Status.STOCK_DONE) {
				updateUser(transaction);
			} else if (transaction.getStatus() == Status.OUT_OF_STOCK) {
				transaction.setStatus(Status.FAILED);
				transaction.setErrorMsg("Not enough quantity left for purchase");
				tranRepo.save(transaction).subscribe();
			}
		} else {
			transaction.setStatus(Status.ABORTED);
			tranRepo.save(transaction).subscribe();
		}

	}

	/**
	 * updateUser if enought balance Or Rollback.
	 *
	 * @param transaction the transaction
	 * @return the mono<? extends user>
	 */
	private void updateUser(Transaction transaction) {
		userRepo.findById(transaction.getUserId()).map(user -> {
			if (user.getAccountBalance() >= transaction.getBill()) {
				user.setAccountBalance(user.getAccountBalance() - transaction.getBill());
				completeTransaction(transaction, user);
			} else {
				transaction.setStatus(Status.ABORTED);
				transaction
						.setErrorMsg("Don't have enough balance for the purchase. Please update your account balance.");
				rollbackTransaction(transaction);
			}
			return user;
		}).subscribe();
	}

	/**
	 * sent rollback event to kafka.
	 *
	 * @param transaction the transaction
	 * @return void
	 */
	private void rollbackTransaction(Transaction transaction) {
		tranRepo.save(transaction).map(tr -> {
			transaction.setErrorMsg("Not enough balance, intiating rollback");
			kafka.sentRollbackStocks(transaction);
			return tr;
		}).subscribe();
	}

	/**
	 * Mark transaction as completed.
	 *
	 * @param transaction the transaction
	 * @param user        the user
	 * @return void
	 */
	private void completeTransaction(Transaction transaction, User user) {
		userRepo.save(user).map(u -> {
			transaction.setStatus(Status.COMPLETED);
			tranRepo.save(transaction).subscribe();
			return u;
		}).subscribe();
	}

	/**
	 * Rollback.
	 *
	 * @param transaction the transaction
	 * @return the mono<? extends user>
	 */
	private Mono<? extends User> rollback(Transaction transaction) {
		kafka.sentRollbackStocks(transaction);
		return Mono.empty();
	}
}