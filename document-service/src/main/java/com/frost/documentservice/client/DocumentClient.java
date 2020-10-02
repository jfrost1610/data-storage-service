package com.frost.documentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.frost.documentservice.model.Documents;

@FeignClient(name = "documentAPI", url = "${feign.apis.document}")
@RequestMapping("/document")
public interface DocumentClient {

	@GetMapping
	public Documents getAllData();

}
