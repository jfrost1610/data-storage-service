/**
 * 
 */
package com.frost.documentservice.model;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Data;

/**
 * @author jobin
 *
 */
@Data
public class ApiError {

	private HttpStatus status;
	private List<String> errors;

	public ApiError(HttpStatus status, String message, List<String> errors) {
		super();
		this.status = status;
		this.errors = errors;
	}

	public ApiError(HttpStatus status, String message, String error) {
		super();
		this.status = status;
		errors = Arrays.asList(error);
	}

	public ApiError(HttpStatus status, List<String> errors) {
		super();
		this.status = status;
		this.errors = errors;
	}

}
