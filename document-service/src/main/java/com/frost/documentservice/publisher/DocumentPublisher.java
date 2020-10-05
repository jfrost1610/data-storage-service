package com.frost.documentservice.publisher;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.frost.documentservice.protobuf.DocumentProtos.DocumentDetailsProto;

/**
 * Provides methods to publish messages to Kafka topic using the
 * {@link DocumentDetailsProto} producer.
 * 
 * @author jobin
 *
 */
@Service
public class DocumentPublisher {

	@Value("${kafka.topic.create}")
	private String createTopic;

	@Value("${kafka.topic.update}")
	private String updateTopic;

	@Autowired
	private Producer<String, DocumentDetailsProto> producer;

	/**
	 * Publishes the {@link DocumentDetailsProto} payload to the Kafka createTopic
	 * 
	 * @param payload
	 *            the create message to be published
	 */
	public void create(DocumentDetailsProto payload) {
		ProducerRecord<String, DocumentDetailsProto> record = new ProducerRecord<>(createTopic, null, payload);
		producer.send(record);
	}

	/**
	 * Publishes the {@link DocumentDetailsProto} payload to the Kafka updateTopic
	 * 
	 * @param payload
	 *            the update message to be published
	 */
	public void update(DocumentDetailsProto payload) {
		ProducerRecord<String, DocumentDetailsProto> record = new ProducerRecord<>(updateTopic, null, payload);
		producer.send(record);
	}

}
