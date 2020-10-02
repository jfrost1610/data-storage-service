/**
 * 
 */
package com.frost.documentservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.frost.documentservice.exception.ValidationException;
import com.frost.documentservice.model.DataModel;
import com.frost.documentservice.model.Documents;
import com.frost.documentservice.service.DocumentService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jobin
 *
 */
@Slf4j
@RestController
@RequestMapping("/data")
public class DataController {

	@Autowired
	private DocumentService documentService;

	@PutMapping
	public @ResponseBody ResponseEntity<String> createData(
			@RequestHeader(value = "fileType", required = true) String fileType,
			@Valid @RequestBody List<DataModel> datas) throws ValidationException {

		if (StringUtils.isBlank(fileType)) {
			throw new ValidationException("FileType Header cannot be empty");
		}

		documentService.addDataToDocument(fileType, datas);

		return ResponseEntity.status(HttpStatus.CREATED).body("Published Document add data to " + fileType);

	}

	@PostMapping
	public @ResponseBody ResponseEntity<String> updateData(
			@RequestHeader(value = "fileType", required = true) String fileType,
			@Valid @RequestBody List<DataModel> datas) throws ValidationException {

		if (StringUtils.isBlank(fileType)) {
			throw new ValidationException("FileType Header cannot be empty");
		}

		log.info("Publising message to update the document.");
		documentService.updateDocument(fileType, datas);

		return ResponseEntity.status(HttpStatus.OK).body("Published Document update data to " + fileType);

	}

	@GetMapping
	public @ResponseBody ResponseEntity<Documents> getData() {
		Documents documents = null;
		
		try {
			documents = documentService.getAllData();
		} catch (Exception e) {
			log.error("Exception occuerd", e);
		}
		log.info("Fetched all data.");

		return ResponseEntity.status(HttpStatus.OK).body(documents);

	}

}
