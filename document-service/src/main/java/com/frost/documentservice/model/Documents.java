/**
 * 
 */
package com.frost.documentservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jobin
 *
 */
@Data
public class Documents {

	private DocumentDetails csvDocument;
	private DocumentDetails xmlDocument;

}
