/**
 * 
 */
package com.frost.storageservice.model;

import java.util.List;

import lombok.Data;

/**
 * @author jobin
 *
 */
@Data
public class DocumentDetails {

	private int size;
	private String type;
	private List<DataModel> datas;
	private List<String> headers;
}
