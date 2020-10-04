/**
 * 
 */
package com.frost.documentservice.model;

import java.util.Objects;

import org.springframework.util.CollectionUtils;

import lombok.Data;

/**
 * @author jobin
 *
 */
@Data
public class Documents {

	private DocumentDetails csvDocument;
	private DocumentDetails xmlDocument;

	public boolean containsCSVData() {
		return containsData(csvDocument);
	}

	public boolean containsXMLData() {
		return containsData(xmlDocument);
	}

	private boolean containsData(DocumentDetails document) {
		return Objects.nonNull(document) && !CollectionUtils.isEmpty(document.getDatas());
	}

}
