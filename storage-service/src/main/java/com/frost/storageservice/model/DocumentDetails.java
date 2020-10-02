/**
 * 
 */
package com.frost.storageservice.model;

import java.util.ArrayList;
import java.util.List;

import com.frost.storageservice.protobuf.DocumentProtos.DocumentDetailsProto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jobin
 *
 */
@NoArgsConstructor
@Data
public class DocumentDetails {
	private int size;
	private String type;
	private List<DataModel> datas;
	private List<String> headers;

	public DocumentDetails(DocumentDetailsProto message) {

		this.size = message.getSize();
		this.type = message.getType();
		this.headers = message.getHeadersList();
		this.datas = new ArrayList<>();
		message.getDatasList().forEach(dataEach -> {
			this.datas.add(new DataModel(dataEach));
		});
	}
}
