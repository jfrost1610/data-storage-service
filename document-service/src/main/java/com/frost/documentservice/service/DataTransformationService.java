/**
 * 
 */
package com.frost.documentservice.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.frost.documentservice.model.DataModel;
import com.frost.documentservice.model.DocumentDetails;
import com.frost.documentservice.model.Documents;
import com.frost.documentservice.protobuf.DocumentProtos.DataModelProto;
import com.frost.documentservice.protobuf.DocumentProtos.DocumentDetailsProto;
import com.frost.documentservice.protobuf.DocumentProtos.DocumentDetailsProto.Builder;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jobin
 *
 */
@Slf4j
@Service
public class DataTransformationService {

	@Autowired
	private CryptoService cryptoService;

	/**
	 * Encrypts the data in the payload and them transforms to
	 * {@link DocumentDetailsProto}
	 * 
	 * @param payload
	 *            the data to encrypt and transform
	 * @return payload with data encrypted and transfomed to proto
	 *         {@link DocumentDetailsProto}
	 */
	public DocumentDetailsProto encryptAndConvertToProto(DocumentDetails payload) {

		Builder builder = DocumentDetailsProto.newBuilder().setSize(payload.getSize());

		if (payload.getType() != null) {
			builder.setType(payload.getType());
		}

		if (!CollectionUtils.isEmpty(payload.getDatas())) {
			builder.addAllDatas(convertToDataModelProtos(payload.getDatas()));
		}

		return builder.build();
	}

	/**
	 * 
	 * @param datas
	 * @return
	 */
	private Iterable<? extends DataModelProto> convertToDataModelProtos(List<DataModel> datas) {

		return datas.stream().filter(Objects::nonNull).map(data -> {

			com.frost.documentservice.protobuf.DocumentProtos.DataModelProto.Builder builder = DataModelProto
					.newBuilder();

			if (data.getId() != null) {
				builder.setId(data.getId());
			}

			if (data.getName() != null) {
				builder.setName(cryptoService.encrypt(data.getName()));
			}

			if (data.getDob() != null) {
				builder.setDob(cryptoService.encrypt(data.getDob()));
			}

			if (data.getSalary() != null) {
				builder.setSalary(cryptoService.encrypt(data.getSalary()));
			}

			return builder.build();

		}).collect(Collectors.toList());
	}

	/**
	 * Method that calls the cryptoService to decrypt the data in {@link Documents}
	 * 
	 * @param documents
	 * @return documents with the data decrypted.
	 */
	public Documents decryptDocumentDatas(Documents documents) {

		log.info("Starting to decrypt DocumentDatas: {}", documents);

		if (documents.containsCSVData()) {
			documents.getCsvDocument().getDatas().forEach(this::decryptDocumentData);
		}

		if (documents.containsXMLData()) {
			documents.getXmlDocument().getDatas().forEach(this::decryptDocumentData);
		}

		return documents;
	}

	/**
	 * @param data
	 */
	private void decryptDocumentData(DataModel data) {
		data.setName(cryptoService.decrypt(data.getName()));
		data.setDob(cryptoService.decrypt(data.getDob()));
		data.setSalary(cryptoService.decrypt(data.getSalary()));
	}

}
