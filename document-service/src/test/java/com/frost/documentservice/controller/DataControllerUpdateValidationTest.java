/**
 * 
 */
package com.frost.documentservice.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.frost.documentservice.model.DataModel;
import com.frost.documentservice.service.DocumentService;
import com.google.gson.Gson;

/**
 * @author jobin
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = DataController.class)
public class DataControllerUpdateValidationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DocumentService documentService;

	@Test
	public void testUpdateDataNameValidationFailure() throws Exception {

		Gson gson = new Gson();
		List<DataModel> datas = new ArrayList<>();
		DataModel data = new DataModel();
		data.setId("1");
		data.setName("");
		data.setDob("1993-01-01");
		data.setSalary("12345");
		datas.add(data);

		String expectedValue = readData("src/test/resources/name-validation-fail-msg.json")
				.replaceAll("CALLING_METHOD_NAME", "updateData");

		RequestBuilder request = MockMvcRequestBuilders.post("/data").contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(datas)).header("fileType", "CSV");
		mockMvc.perform(request).andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(content().string(expectedValue)).andReturn();

	}
	
	@Test
	public void testUpdateDataIdValidationFailure() throws Exception {

		Gson gson = new Gson();
		List<DataModel> datas = new ArrayList<>();
		DataModel data = new DataModel();
		data.setId("");
		data.setName("Test");
		data.setDob("1993-01-01");
		data.setSalary("12345");
		datas.add(data);

		String expectedValue = readData("src/test/resources/id-validation-fail-msg.json")
				.replaceAll("CALLING_METHOD_NAME", "updateData");

		RequestBuilder request = MockMvcRequestBuilders.post("/data").contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(datas)).header("fileType", "CSV");
		mockMvc.perform(request).andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(content().string(expectedValue)).andReturn();

	}

	@Test
	public void testUpdateDataAgeValidationFailure() throws Exception {

		Gson gson = new Gson();
		List<DataModel> datas = new ArrayList<>();
		DataModel data = new DataModel();
		data.setId("1");
		data.setName("Test");
		data.setDob("2010-01-01");
		data.setSalary("12345");
		datas.add(data);

		String expectedValue = readData("src/test/resources/age-validation-fail-msg.json")
				.replaceAll("CALLING_METHOD_NAME", "updateData");

		RequestBuilder request = MockMvcRequestBuilders.post("/data").contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(datas)).header("fileType", "CSV");
		mockMvc.perform(request).andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(content().string(expectedValue)).andReturn();

	}

	@Test
	public void testUpdateDataDobSalaryValidationFailure() throws Exception {

		Gson gson = new Gson();
		List<DataModel> datas = new ArrayList<>();
		DataModel data = new DataModel();
		data.setId("1");
		data.setName("Test");
		data.setDob("2000-01-01");
		datas.add(data);

		String expectedValue = readData("src/test/resources/salary-validation-fail-msg.json")
				.replaceAll("CALLING_METHOD_NAME", "updateData");

		RequestBuilder request = MockMvcRequestBuilders.post("/data").contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(datas)).header("fileType", "CSV");
		mockMvc.perform(request).andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(content().string(expectedValue)).andReturn();

	}

	@Test
	public void testUpdateDataHeaderValidationFail() throws Exception {

		Gson gson = new Gson();
		List<DataModel> datas = new ArrayList<>();

		String invalidHeaderValue = "test-header";

		String expectedValue = readData("src/test/resources/header-validation-fail-msg.json").replace("ACTUAL_VALUE",
				invalidHeaderValue);

		RequestBuilder request = MockMvcRequestBuilders.post("/data").contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(datas)).header("fileType", invalidHeaderValue);
		mockMvc.perform(request).andExpect(status().is(400)).andExpect(content().string(expectedValue)).andReturn();

	}

	public String readData(String fileName) {

		try {
			return new String(Files.readAllBytes(Paths.get(fileName)));
		} catch (IOException e) {
			Assert.fail("FAiled to read the file " + fileName);
			return null;
		}

	}

}
