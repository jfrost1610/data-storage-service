package com.frost.documentservice.publisher;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.frost.documentservice.model.DataModel;
import com.frost.documentservice.model.DocumentDetails;
import com.frost.documentservice.protobuf.DocumentProtos.DataModelProto;
import com.frost.documentservice.protobuf.DocumentProtos.DocumentDetailsProto;
import com.frost.documentservice.protobuf.DocumentProtos.DocumentDetailsProto.Builder;

@Service
public class DocumentPublisher {

	@Value("${kafka.topic.create}")
	private String createTopic;

	@Value("${kafka.topic.update}")
	private String updateTopic;

	@Autowired
	Producer<String, DocumentDetailsProto> producer;

	public void create(DocumentDetails payload) {
		DocumentDetailsProto protoPayload = convertToDocumentDetailsProto(payload);
		ProducerRecord<String, DocumentDetailsProto> record = new ProducerRecord<>(createTopic, null, protoPayload);
		producer.send(record);
	}

	public void update(DocumentDetails payload) {
		DocumentDetailsProto protoPayload = convertToDocumentDetailsProto(payload);
		ProducerRecord<String, DocumentDetailsProto> record = new ProducerRecord<>(updateTopic, null, protoPayload);
		producer.send(record);
	}

	private DocumentDetailsProto convertToDocumentDetailsProto(DocumentDetails payload) {

		Builder builder = DocumentDetailsProto.newBuilder().setSize(payload.getSize());

		if (payload.getType() != null) {
			builder.setType(payload.getType());
		}

		if (!CollectionUtils.isEmpty(payload.getDatas())) {
			builder.addAllDatas(convertToDataModelProtos(payload.getDatas()));
		}

		if (!CollectionUtils.isEmpty(payload.getHeaders())) {
			builder.addAllHeaders(payload.getHeaders());
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
				builder.setName(data.getName());
			}

			if (data.getDob() != null) {
				builder.setDob(data.getDob());
			}

			if (data.getSalary() != null) {
				builder.setSalary(data.getSalary());
			}

			return builder.build();

		}).collect(Collectors.toList());
	}

}
