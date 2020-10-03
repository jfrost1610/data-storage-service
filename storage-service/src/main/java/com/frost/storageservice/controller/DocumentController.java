/**
 * 
 */
package com.frost.storageservice.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.frost.storageservice.model.Documents;
import com.frost.storageservice.service.DocumentService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jobin
 *
 */
@Slf4j
@RestController
@RequestMapping("/document")
public class DocumentController {

	@Autowired
	private DocumentService documentService;

	@GetMapping
	public @ResponseBody ResponseEntity<Documents> readData() throws IOException {

		log.info("Starting to read Data.");

		Documents documents = documentService.getAllData();

		log.info("Completed to Fetch all Data.");

		return ResponseEntity.status(HttpStatus.OK).body(documents);

	}

}
