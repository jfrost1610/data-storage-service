/**
 * 
 */
package com.frost.storageservice.writer;

import java.io.IOException;

import com.frost.storageservice.model.DocumentDetails;

/**
 * A File Writer Interface that provides methods to manipulate Data on a
 * DataFile.
 * 
 * @author jobin
 *
 */
public interface DataWriter {

	/**
	 * Intializes the DataFile and does initial setup.
	 * 
	 * @throws IOException
	 */
	void initialize() throws IOException;

	/**
	 * Writes the data to the DataFile.
	 * 
	 * @param document
	 * @return
	 */
	boolean write(DocumentDetails document);

	/**
	 * Updates the data on the DataFile.
	 * 
	 * @param document
	 * @return
	 */
	boolean update(DocumentDetails document);

	/**
	 * Returns the storage location of the DataFile.
	 * 
	 * @return
	 */
	String getFilePath();

	/**
	 * Fetches all Data from the DataFile
	 * 
	 * @return
	 * @throws IOException
	 */
	DocumentDetails readAll() throws IOException;

}
