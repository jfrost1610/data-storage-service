/**
 * 
 */
package com.frost.storageservice.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frost.storageservice.model.DocumentDetails;
import com.frost.storageservice.model.Documents;
import com.frost.storageservice.protobuf.DocumentProtos.DocumentDetailsProto;

/**
 * @author jobin
 *
 */
@Service
public class DataTransformationService {

	@Autowired
	private CryptoService cryptoService;

	public DocumentDetails parseAndDecryptDocumentProto(DocumentDetailsProto documentDetailsProto) {

		DocumentDetails documentDetails = new DocumentDetails(documentDetailsProto);
		documentDetails.getDatas().forEach(data -> {

			data.setName(cryptoService.decrypt(data.getName()));
			data.setDob(cryptoService.decrypt(data.getDob()));
			data.setSalary(cryptoService.decrypt(data.getSalary()));

		});

		return documentDetails;
	}

	public Documents encryptAndConvertToDocumentsProto(DocumentDetails csvDocument, DocumentDetails xmlDocument) {
		return new Documents(encryptDocumentDetails(csvDocument), encryptDocumentDetails(xmlDocument));
	}

	/**
	 * @param document
	 */
	private DocumentDetails encryptDocumentDetails(DocumentDetails document) {

		if (Objects.nonNull(document)) {
			document.getDatas().forEach(data -> {

				data.setName(cryptoService.encrypt(data.getName()));
				data.setDob(cryptoService.encrypt(data.getDob()));
				data.setSalary(cryptoService.encrypt(data.getSalary()));

			});
		}

		return document;
	}

}
