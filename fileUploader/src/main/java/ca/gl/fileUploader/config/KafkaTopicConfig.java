package ca.gl.fileUploader.config;

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

}
