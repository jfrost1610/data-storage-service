/**
 * 
 */
package com.frost.storageservice.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

	/**
	 * Method that calls the cryptoService to decrypt the data in
	 * {@link DocumentDetailsProto}
	 * 
	 * @param documentDetailsProto
	 * 
	 * @return
	 */
	public DocumentDetails parseAndDecryptDocumentProto(DocumentDetailsProto documentDetailsProto) {

		DocumentDetails documentDetails = new DocumentDetails(documentDetailsProto);
		documentDetails.getDatas().forEach(data -> {

			data.setName(cryptoService.decrypt(data.getName()));
			data.setDob(cryptoService.decrypt(data.getDob()));
			data.setSalary(cryptoService.decrypt(data.getSalary()));

		});

		return documentDetails;
	}

	/**
	 * Method that calls the cryptoService to encrypt the data in csvDocument and
	 * xmlDocument and then transforms them to {@link Documents}
	 * 
	 * @param csvDocument
	 * @param xmlDocument
	 * @return
	 */
	public Documents encryptAndConvertToDocuments(DocumentDetails csvDocument, DocumentDetails xmlDocument) {
		return new Documents(encryptDocumentDetails(csvDocument), encryptDocumentDetails(xmlDocument));
	}

	/**
	 * @param document
	 */
	private DocumentDetails encryptDocumentDetails(DocumentDetails document) {

		if (Objects.nonNull(document) && !CollectionUtils.isEmpty(document.getDatas())) {
			document.getDatas().forEach(data -> {

				data.setName(cryptoService.encrypt(data.getName()));
				data.setDob(cryptoService.encrypt(data.getDob()));
				data.setSalary(cryptoService.encrypt(data.getSalary()));

			});
		}

		return document;
	}

}
