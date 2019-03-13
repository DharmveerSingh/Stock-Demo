package ca.gl.fileUploader.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import ca.gl.fileUploader.model.Stock;

/**
 * The Class MessageProducer.
 */
@Component
public class MessageProducer {

	/** The stock kafka template. */
	@Autowired
	private KafkaTemplate<String, Stock> stockKafkaTemplate;

	/** The topic name. */
	@Value(value = "${message.topic.name}")
	private String topicName;

	/**
	 * Sent stocks.
	 *
	 * @param stock the stock
	 */
	public void sentStocks(Stock stock) {
		stockKafkaTemplate.send(topicName, stock);
	}
}