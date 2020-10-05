package com.frost.documentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.frost.documentservice.model.Documents;

/**
 * Feign client that provides method to connect to the APIs provided by storage
 * service
 * 
 * @author jobin
 *
 */
@FeignClient(name = "storageAPI", url = "${feign.apis.storage}")
@RequestMapping("/document")
public interface DocumentClient {

	/**
	 * Called the storageAPIs Restful endppoint to fetch all data.
	 * 
	 * @return {@link Documents}
	 */
	@GetMapping
	public Documents getAllData();

}
