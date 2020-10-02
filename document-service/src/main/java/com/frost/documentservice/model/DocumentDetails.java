/**
 * 
 */
package com.frost.documentservice.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jobin
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDetails {

	private int size;
	private String type;
	private List<DataModel> datas;
	private List<String> headers;

}
