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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.frost.documentservice.model.DataModel;
import com.frost.documentservice.model.DocumentDetails;
import com.frost.documentservice.model.Documents;
import com.frost.documentservice.service.DocumentService;
import com.google.gson.Gson;

/**
 * @author jobin
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = DataController.class)
public class DataControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DocumentService documentService;

	@Test
	public void testCreateDataXML() throws Exception {

		Gson gson = new Gson();
		List<DataModel> datas = new ArrayList<>();

		RequestBuilder request = MockMvcRequestBuilders.put("/data").contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(datas)).header("fileType", "XML");
		mockMvc.perform(request).andExpect(status().is(HttpStatus.CREATED.value()))
				.andExpect(content().string("Published Document add data to XML")).andReturn();

	}

	@Test
	public void testCreateDataCSV() throws Exception {

		Gson gson = new Gson();
		List<DataModel> datas = new ArrayList<>();

		RequestBuilder request = MockMvcRequestBuilders.put("/data").contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(datas)).header("fileType", "CSV");
		mockMvc.perform(request).andExpect(status().is(HttpStatus.CREATED.value()))
				.andExpect(content().string("Published Document add data to CSV")).andReturn();

	}

	@Test
	public void testUpdateDataXML() throws Exception {

		Gson gson = new Gson();
		List<DataModel> datas = new ArrayList<>();
		DataModel data = new DataModel();
		data.setId("1");
		data.setName("TestName");
		data.setDob("1993-01-01");
		data.setSalary("12345");
		datas.add(data);

		RequestBuilder request = MockMvcRequestBuilders.post("/data").contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(datas)).header("fileType", "XML");
		mockMvc.perform(request).andExpect(status().is(HttpStatus.OK.value()))
				.andExpect(content().string("Published Document update data to XML")).andReturn();

	}

	@Test
	public void testUpdateDataCSV() throws Exception {

		Gson gson = new Gson();
		List<DataModel> datas = new ArrayList<>();
		DataModel data = new DataModel();
		data.setId("1");
		data.setName("TestName");
		data.setDob("1993-01-01");
		data.setSalary("12345");
		datas.add(data);

		RequestBuilder request = MockMvcRequestBuilders.post("/data").contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(datas)).header("fileType", "CSV");
		mockMvc.perform(request).andExpect(status().is(HttpStatus.OK.value()))
				.andExpect(content().string("Published Document update data to CSV")).andReturn();

	}

	@Test
	public void testGetData() throws Exception {

		Documents data = new Documents();
		DocumentDetails csvDocument = new DocumentDetails();
		csvDocument.setSize(10);
		DocumentDetails xmlDocument = new DocumentDetails();
		xmlDocument.setSize(20);
		data.setCsvDocument(csvDocument);
		data.setXmlDocument(xmlDocument);

		Mockito.when(documentService.getAllData()).thenReturn(data);

		RequestBuilder request = MockMvcRequestBuilders.get("/data");
		MvcResult result = mockMvc.perform(request).andExpect(status().is(HttpStatus.OK.value())).andReturn();

		Gson gson = new Gson();
		Documents responseBody = gson.fromJson(result.getResponse().getContentAsString(), Documents.class);

		Assert.assertNotNull(responseBody);
		Assert.assertNotNull(responseBody.getCsvDocument());
		Assert.assertNotNull(responseBody.getXmlDocument());
		Assert.assertEquals(10, responseBody.getCsvDocument().getSize());
		Assert.assertEquals(20, responseBody.getXmlDocument().getSize());

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
