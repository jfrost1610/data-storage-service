/**
 * 
 */
package com.frost.storageservice.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.frost.storageservice.model.DocumentDetails;
import com.frost.storageservice.protobuf.DocumentProtos.DocumentDetailsProto;
import com.frost.storageservice.service.DocumentService;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jobin
 *
 */
@Slf4j
@Service
public class DataProcessor {

	@Autowired
	private DocumentService documentService;

	@KafkaListener(topics = "${kafka.topic.create}")
	public void addData(DocumentDetailsProto message) {

		// Gson gson = new Gson();
		// DocumentDetails documentDetails = gson.fromJson(message,
		// DocumentDetails.class);

		log.info("Starting to add Data. {}", message);

		// documentService.addDataToDocument(documentDetails);

		log.info("Completed to add Data.");

	}

	@KafkaListener(topics = "${kafka.topic.update}")
	public void updateData(String message) {

		Gson gson = new Gson();
		DocumentDetails documentDetails = gson.fromJson(message, DocumentDetails.class);

		log.info("Starting to add Data.");

		documentService.updateDataOnDocument(documentDetails);

		log.info("Completed to update Data.");

	}

}
