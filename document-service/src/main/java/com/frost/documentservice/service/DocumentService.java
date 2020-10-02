package com.frost.documentservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frost.documentservice.client.DocumentClient;
import com.frost.documentservice.model.DataModel;
import com.frost.documentservice.model.DocumentDetails;
import com.frost.documentservice.model.Documents;
import com.frost.documentservice.publisher.DocumentPublisher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DocumentService {

	@Autowired
	private DocumentClient client;

	@Autowired
	private DocumentPublisher publisher;

	public void addDataToDocument(String fileType, List<DataModel> datas) {
		DocumentDetails newDocument = DocumentDetails.builder().type(fileType).datas(datas).build();
		log.info("Publishing Create Message to topic.");
		publisher.create(newDocument);

	}

	public void updateDocument(String fileType, List<DataModel> datas) {
		DocumentDetails documentDetails = DocumentDetails.builder().type(fileType).datas(datas).build();
		documentDetails.setDatas(datas);
		log.info("Publishing Update Message to topc.");
		publisher.update(documentDetails);

	}

	public Documents getAllData() {
		log.info("Calling Document client to fetch all data");
		return client.getAllData();
	}

}
