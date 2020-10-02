package com.frost.documentservice.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.frost.documentservice.model.DocumentDetails;
import com.google.gson.Gson;

@Service
public class DocumentPublisher {

	@Value("${kafka.topic.create}")
	private String createTopic;

	@Value("${kafka.topic.update}")
	private String updateTopic;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void create(DocumentDetails payload) {
		Gson gson = new Gson();
		kafkaTemplate.send(createTopic, gson.toJson(payload));
	}

	public void update(DocumentDetails payload) {
		Gson gson = new Gson();
		kafkaTemplate.send(updateTopic, gson.toJson(payload));
	}

}
