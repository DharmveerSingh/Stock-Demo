package ca.gl.srs.kafka.producer;

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
	@Value(value = "${message.topic.name.buy.result}")
	private String topicBuyResult;
	
	/**
	 * Sent B.
	 * @param transaction the stock
	 */
	public void sentBuyResult(Transaction transaction) {
		stockKafkaTemplate.send(topicBuyResult, transaction);
	}
}