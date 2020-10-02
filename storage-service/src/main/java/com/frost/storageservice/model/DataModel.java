/**
 * 
 */
package com.frost.storageservice.model;

import com.frost.storageservice.protobuf.DocumentProtos.DataModelProto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jobin
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataModel {

	private String id;
	private String name;
	private String dob;
	private String salary;

	public DataModel(DataModelProto data) {

		this.id = data.getId();
		this.name = data.getName();
		this.dob = data.getDob();
		this.salary = data.getSalary();

	}

}
