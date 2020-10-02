/**
 * 
 */
package com.frost.storageservice.writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author jobin
 *
 */
@Component
public class DataWriterFactory {

	@Autowired
	private ApplicationContext ctx;

	public DataWriter getWriter(String type) {
		return (DataWriter) ctx.getBean(type.toUpperCase());
	}

}
