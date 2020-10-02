/**
 * 
 */
package com.frost.storageservice.writer;

import java.io.IOException;

import com.frost.storageservice.model.DocumentDetails;

/**
 * @author jobin
 *
 */
public interface DataWriter {

	void initialize() throws IOException;

	boolean write(DocumentDetails document);

	boolean update(DocumentDetails document);

	String getFilePath();

	DocumentDetails readAll() throws IOException;

}
