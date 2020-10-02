/**
 * 
 */
package com.frost.documentservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jobin
 *
 */
@Configuration
public class KafkaConfig {

	@Value("${kafka.topic.create}")
	private String creationTopic;

	@Value("${kafka.topic.update}")
	private String updateTopic;

	@Bean
	public NewTopic createDataUpdateTopic() {
		return new NewTopic(creationTopic, 3, (short) 1);
	}

	@Bean
	public NewTopic createDataCreateTopic() {
		return new NewTopic(updateTopic, 3, (short) 1);
	}

}
