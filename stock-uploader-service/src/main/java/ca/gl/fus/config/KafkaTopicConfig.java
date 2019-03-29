package ca.gl.fus.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

/**
 * The Class KafkaTopicConfig.
 */
@Configuration
public class KafkaTopicConfig {

	/** The bootstrap address. */
	@Value(value = "${kafka.bootstrapAddress}")
	private String bootstrapAddress;

	/** The topic name. */
	@Value("${message.topic.name}")
	private String topicName;

	/** The buy result. */
	@Value("${message.topic.name.buy.result}")
	private String buyResult;

	/** The buy error. */
	@Value("${message.topic.name.buy.error}")
	private String buyError;

	/** The buy stocks. */
	@Value("${message.topic.name.buy.stocks}")
	private String buyStocks;

	/** The buy rollback. */
	@Value("${message.topic.name.buy.rollback}")
	private String buyRollback;

	
	/** The replication factor. */
	@Value("${message.topic.replicationFator}")
	private short replicationFactor;

	/**
	 * Kafka admin.
	 *
	 * @return the kafka admin
	 */
	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		return new KafkaAdmin(configs);
	}

	/**
	 * Topic 1.
	 *
	 * @return the new topic
	 */
	@Bean
	public NewTopic topic1() {
		return new NewTopic(topicName, 1, replicationFactor);
	}
	
	/**
	 * Buy result.
	 *
	 * @return the new topic
	 */
	@Bean
	public NewTopic buyResult() {
		return new NewTopic(buyResult, 1, replicationFactor);
	}
	
	/**
	 * Buy error.
	 *
	 * @return the new topic
	 */
	@Bean
	public NewTopic buyError() {
		return new NewTopic(buyError, 1, replicationFactor);
	}
	
	/**
	 * Buy stocks.
	 *
	 * @return the new topic
	 */
	@Bean
	public NewTopic buyStocks() {
		return new NewTopic(buyStocks, 1, replicationFactor);
	}
	
	/**
	 * Buy rollback.
	 *
	 * @return the new topic
	 */
	@Bean
	public NewTopic buyRollback() {
		return new NewTopic(buyRollback, 1, replicationFactor);
	}
	

}
