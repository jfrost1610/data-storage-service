package com.frost.documentservice.exception;

/**
 * @author jobin
 *
 */
public class EncryptionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EncryptionException() {
		super();
	}

	public EncryptionException(String msg) {
		super(msg);
	}

	public EncryptionException(String string, Exception e) {
		super(string, e);
	}

}
