/**
 * 
 */
package com.frost.documentservice.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.frost.documentservice.validator.Age;
import com.frost.documentservice.validator.Date;

import lombok.Data;

/**
 * @author jobin
 *
 */
@Data
public class DataModel {

	private String id;

	@NotNull(message = "Name cannot be null")
	@NotBlank(message = "Name cannot be empty")
	private String name;

	@NotNull(message = "Dob cannot be null")
	@Date
	@Age(value = 18)
	private String dob;

	@NotNull(message = "Salary cannot be null")
	@Min(value = 1, message = "Salary should be more than or equal to 1")
	private String salary;

}
