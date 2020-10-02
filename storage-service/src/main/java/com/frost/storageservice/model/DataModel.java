/**
 * 
 */
package com.frost.storageservice.model;

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

}
