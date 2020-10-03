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

/**
 * @author jobin
 *
 */
@Service
public class DataTransformationService {

	@Autowired
	private CryptoService cryptoService;

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

	public Documents decryptDocumentDatas(Documents documents) {

		if (Objects.nonNull(documents.getCsvDocument())) {
			documents.getCsvDocument().getDatas().forEach(this::decryptDocumentData);
		}

		if (Objects.nonNull(documents.getXmlDocument())) {
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
