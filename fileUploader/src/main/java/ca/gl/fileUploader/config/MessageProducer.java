package ca.gl.fileUploader.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import ca.gl.fileUploader.model.Stock;

public class MessageProducer {

	@Autowired
	private KafkaTemplate<String, Stock> stockKafkaTemplate;

	@Value(value = "${message.topic.name}")
	private String topicName;

	public void sentStocks(Stock stock) {
		stockKafkaTemplate.send(topicName, stock);
	}
}