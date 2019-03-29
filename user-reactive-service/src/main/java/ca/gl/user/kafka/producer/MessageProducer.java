package ca.gl.user.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import ca.gl.user.model.transaction.Transaction;

/**
 * The Class MessageProducer.
 */
@Component
public class MessageProducer {

	/** The stock kafka template. */
	@Autowired
	private KafkaTemplate<String, Transaction> stockKafkaTemplate;

	/** The topic topicBuyStocks. */
	@Value(value = "${message.topic.name.buy.stocks}")
	private String topicBuyStocks;
	
	/** The topic topicBuyRollBack. */
	@Value(value="${message.topic.name.buy.rollback}")
	private String topicBuyRollBack;

	/**
	 * Sent stocks.
	 *
	 * @param transaction the stock
	 */
	public void sentRollbackStocks(Transaction transaction) {
		stockKafkaTemplate.send(topicBuyRollBack, transaction);
	}
	
	/**
	 * Sent B.
	 *
	 * @param transaction the stock
	 */
	public void sentBuyStocks(Transaction transaction) {
		stockKafkaTemplate.send(topicBuyStocks, transaction);
	}
}