/**
 * 
 */
package com.frost.documentservice.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@Validated
@Slf4j
@RestController
@RequestMapping("/data")
public class DataController {

	@Autowired
	private DocumentService documentService;

	private static final List<String> validFileTypes = Arrays.asList("CSV", "XML");

	/**
	 * Rest endpoint to add data to a data file
	 * 
	 * @param fileType
	 *            header that identifies the type of data file to add data to.
	 *            Supports CSV and XML
	 * @param datas
	 *            the list of {@link DataModel} to add to the data file
	 * @return
	 */
	@PutMapping
	public @ResponseBody ResponseEntity<String> createData(
			@RequestHeader(value = "fileType", required = true) String fileType,
			@Valid @RequestBody List<DataModel> datas) {

		headerValidations(fileType);

		documentService.addDataToDocument(fileType, datas);

		return ResponseEntity.status(HttpStatus.CREATED).body("Published Document add data to " + fileType);

	}

	/**
	 * Rest endpoint to update data in a data file
	 * 
	 * @param fileType
	 *            header that identifies the type of data file to update data in.
	 *            Supports CSV and XML
	 * @param datas
	 *            the list of {@link DataModel} to be updated in the data file
	 * @return
	 */
	@PostMapping
	public @ResponseBody ResponseEntity<String> updateData(
			@RequestHeader(value = "fileType", required = true) String fileType,
			@Valid @RequestBody List<DataModel> datas) {

		headerValidations(fileType);
		idValidation(datas);

		log.info("Publising message to update the document.");
		documentService.updateDocument(fileType, datas);

		return ResponseEntity.status(HttpStatus.OK).body("Published Document update data to " + fileType);

	}

	/**
	 * Rest endpoint to fetch data from all the datafiles ie. CSV and XML Data file
	 * 
	 * @return
	 */
	@GetMapping
	public @ResponseBody ResponseEntity<Documents> getData() {

		log.info("Starting to fetch all data.");

		Documents documents = documentService.getAllData();

		log.info("Fetched all data.");

		return ResponseEntity.status(HttpStatus.OK).body(documents);

	}

	/**
	 * Validates the fileType Header
	 * 
	 * @param fileType
	 * @throws ValidationException
	 */
	private void headerValidations(String fileType) {

		Map<String, String> errorMap = new HashMap<>();

		if (!validFileTypes.contains(fileType.toUpperCase())) {
			errorMap.put("fileType",
					"Invalid FileType Header value [" + fileType + "]. Valid types are " + validFileTypes);
		}

		if (!errorMap.isEmpty()) {
			throw new ValidationException("Header Validations Failed", errorMap);
		}

	}

	/**
	 * Validates id field on DataModels to be updated
	 * 
	 * @param datas
	 */
	private void idValidation(List<DataModel> datas) {

		Map<String, String> errorMap = new HashMap<>();

		for (int i = 0; i < datas.size(); i++) {
			if (StringUtils.isBlank(datas.get(i).getId())) {
				errorMap.put("DataModel[" + i + "]", "id is a Mandatory field");
			}
		}

		if (!errorMap.isEmpty()) {
			throw new ValidationException("id is a Mandatory field!", errorMap);
		}

	}

}
