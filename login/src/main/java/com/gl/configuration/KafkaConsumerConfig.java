package com.gl.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import ca.gl.fileUploader.model.Stock;

/**
 * the kafka consumer config.
 *
 * @author dharamveer.singh
 */
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

	/** The bootstrap address. */
	@Value(value = "${kafka.bootstrapAddress}")
	private String bootstrapAddress;

	/**
	 * Consumer factory.
	 *
	 * @param groupId the group id
	 * @return the consumer factory
	 */
	public ConsumerFactory<String, String> consumerFactory(String groupId) {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(props);
	}

	/**
	 * Foo kafka listener container factory.
	 *
	 * @return the concurrent kafka listener container factory
	 */
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> fooKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory("stockGroup"));
		return factory;
	}

	/**
	 * Stock consumer factory.
	 *
	 * @return the consumer factory
	 */
	public ConsumerFactory<String, Stock> stockConsumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "stockGroup");
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "kafka.producer");

		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
				new JsonDeserializer<Stock>(Stock.class));
	}

	/**
	 * Greeting kafka listener container factory.
	 *
	 * @return the concurrent kafka listener container factory
	 */
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Stock> greetingKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Stock> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(stockConsumerFactory());
		return factory;
	}

}
