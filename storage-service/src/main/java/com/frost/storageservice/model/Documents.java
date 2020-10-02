/**
 * 
 */
package com.frost.storageservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jobin
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Documents {

	private DocumentDetails csvDocument;
	private DocumentDetails xmlDocument;

}
