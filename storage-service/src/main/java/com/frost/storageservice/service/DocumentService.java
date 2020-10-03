package com.frost.storageservice.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frost.storageservice.model.DocumentDetails;
import com.frost.storageservice.model.Documents;
import com.frost.storageservice.protobuf.DocumentProtos.DocumentDetailsProto;
import com.frost.storageservice.writer.DataWriterFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DocumentService {

	@Autowired
	private DataWriterFactory writerFactory;

	@Autowired
	private DataTransformationService dataTransformationService;

	public void addDataToDocument(DocumentDetailsProto message) {

		DocumentDetails documentDetails = dataTransformationService.parseAndDecryptDocumentProto(message);

		log.info("Getting Writer!");
		boolean status = writerFactory.getWriter(documentDetails.getType()).write(documentDetails);

		log.info("Add Data to Document status : {}", status);
	}

	public void updateDataOnDocument(DocumentDetailsProto message) {

		DocumentDetails documentDetails = dataTransformationService.parseAndDecryptDocumentProto(message);

		log.info("Getting Writer!");
		boolean status = writerFactory.getWriter(documentDetails.getType()).update(documentDetails);

		log.info("Update Data to Document status : {}", status);
	}

	public Documents getAllData() throws IOException {

		log.info("Getting all Data!");
		
		DocumentDetails csvDocument = writerFactory.getWriter("CSV").readAll();
		DocumentDetails xmlDocument = null;// writerFactory.getWriter("XML").readAll();
		
		log.info("Fetched all file data");

		return dataTransformationService.encryptAndConvertToDocumentsProto(csvDocument, xmlDocument);
	}

}
