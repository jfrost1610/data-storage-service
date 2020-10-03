package com.frost.storageservice.exception;

/**
 * @author jobin
 *
 */
public class WriterException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WriterException() {
		super();
	}

	public WriterException(String msg) {
		super(msg);
	}

	public WriterException(String string, Exception e) {
		super(string, e);
	}

}
