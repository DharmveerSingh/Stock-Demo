package ca.gl.fileUploader.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import ca.gl.fileUploader.model.Stock;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageProducer.
 */
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