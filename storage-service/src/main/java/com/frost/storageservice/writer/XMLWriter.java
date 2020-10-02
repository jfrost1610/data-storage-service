/**
 * 
 */
package com.frost.storageservice.writer;

import org.springframework.stereotype.Service;

import com.frost.storageservice.model.DocumentDetails;

/**
 * @author jobin
 *
 */
@Service("XML")
public class XMLWriter implements DataWriter {

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean write(DocumentDetails document) {
		return false;
	}

	@Override
	public boolean update(DocumentDetails document) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DocumentDetails readAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilePath() {
		// TODO Auto-generated method stub
		return null;
	}

}
