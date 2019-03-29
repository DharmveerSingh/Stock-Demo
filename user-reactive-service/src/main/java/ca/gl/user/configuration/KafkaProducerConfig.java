package ca.gl.user.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import ca.gl.user.model.transaction.Transaction;

/**
 * The Class KafkaProducerConfig.
 */
@Configuration
public class KafkaProducerConfig {

	/** The bootstrap address. */
	@Value(value = "${kafka.bootstrapAddress}")
	private String bootstrapAddress;

	/**
	 * Transaction producer factory.
	 * @return the producer factory
	 */
	@Bean
	public ProducerFactory<String, Transaction> stockProducerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	/**
	 * Transaction kafka template.
	 * @return the kafka template
	 */
	@Bean
	public KafkaTemplate<String, Transaction> stockKafkaTemplate() {
		return new KafkaTemplate<>(stockProducerFactory());
	}

}