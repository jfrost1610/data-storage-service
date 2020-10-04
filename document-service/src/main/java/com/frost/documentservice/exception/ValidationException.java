package com.frost.documentservice.exception;

import java.util.Map;

/**
 * @author jobin
 *
 */
public class ValidationException extends RuntimeException {

	private Map<String, String> validationErrors;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ValidationException() {
		super();
	}

	public ValidationException(String msg, Map<String, String> validationErrors) {
		super(msg);
		this.validationErrors = validationErrors;
	}

	/**
	 * @return the validationErrors
	 */
	public Map<String, String> getValidationErrors() {
		return validationErrors;
	}

}
