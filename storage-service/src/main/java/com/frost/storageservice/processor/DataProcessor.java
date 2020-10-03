/**
 * 
 */
package com.frost.storageservice.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.frost.storageservice.protobuf.DocumentProtos.DocumentDetailsProto;
import com.frost.storageservice.service.DocumentService;

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

		log.info("Starting to add Data. {}", message);

		documentService.addDataToDocument(message);

		log.info("Completed to add Data.");

	}

	@KafkaListener(topics = "${kafka.topic.update}")
	public void updateData(DocumentDetailsProto message) {

		log.info("Starting to update Data.", message);

		documentService.updateDataOnDocument(message);

		log.info("Completed to update Data.");

	}

}
