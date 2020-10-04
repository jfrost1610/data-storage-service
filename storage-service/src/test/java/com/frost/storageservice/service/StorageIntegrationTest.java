/**
 * 
 */
package com.frost.storageservice.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Files;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.frost.storageservice.model.DataModel;
import com.frost.storageservice.model.Documents;
import com.frost.storageservice.processor.DataProcessor;
import com.frost.storageservice.protobuf.DocumentProtos.DataModelProto;
import com.frost.storageservice.protobuf.DocumentProtos.DocumentDetailsProto;
import com.frost.storageservice.writer.CSVDataWriter;
import com.frost.storageservice.writer.DataWriterFactory;
import com.frost.storageservice.writer.XMLDataWriter;

/**
 * @author jobin
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DataProcessor.class, DocumentService.class, DataWriterFactory.class,
		DataTransformationService.class, CSVDataWriter.class, XMLDataWriter.class })
@TestPropertySource(properties = { "storage.path=src/test/resources" })
public class StorageIntegrationTest {

	@Autowired
	private DataProcessor dataProcessor;

	@Autowired
	private DocumentService documentService;

	@Autowired
	private CSVDataWriter csvDataWriter;

	@Autowired
	private XMLDataWriter xmlDataWriter;

	@MockBean
	private CryptoService cryptoService;

	@After
	public void cleanUp() {
		Files.delete(new File(csvDataWriter.getFilePath()));
		Files.delete(new File(xmlDataWriter.getFilePath()));
	}

	@Test
	public void testIntegrationComplete() throws IOException {

		Mockito.when(cryptoService.encrypt(ArgumentMatchers.anyString())).thenAnswer(arg -> arg.getArgument(0));
		Mockito.when(cryptoService.decrypt(ArgumentMatchers.anyString())).thenAnswer(arg -> arg.getArgument(0));

		// Add two records to CSV File

		List<DataModelProto> datasForCSV = new ArrayList<>();
		datasForCSV.add(createTestDataModel("TestUser1", "1993-10-01", "10000"));
		datasForCSV.add(createTestDataModel("TestUser2", "1997-10-12", "35000"));

		DocumentDetailsProto csvPayloadData = DocumentDetailsProto.newBuilder().setType("csv").addAllDatas(datasForCSV)
				.build();

		dataProcessor.addData(csvPayloadData);

		// Add three records to XML File

		List<DataModelProto> datasForXML = new ArrayList<>();
		datasForXML.add(createTestDataModel("TestUser3", "1996-10-01", "20000"));
		datasForXML.add(createTestDataModel("TestUser4", "1999-10-12", "45000"));
		datasForXML.add(createTestDataModel("TestUser5", "2005-03-09", "80000"));

		DocumentDetailsProto xmlPayloadData = DocumentDetailsProto.newBuilder().setType("xMl").addAllDatas(datasForXML)
				.build();

		dataProcessor.addData(xmlPayloadData);

		// Read all Data and verify

		Documents documents = documentService.getAllData();

		Assert.assertEquals(2, documents.getCsvDocument().getSize());
		Assert.assertEquals(3, documents.getXmlDocument().getSize());

		// Add one more records to CSV File

		List<DataModelProto> datasForCSV2 = new ArrayList<>();
		datasForCSV2.add(createTestDataModel("TestUser3", "1993-07-12", "80000"));

		DocumentDetailsProto csvPayloadData2 = DocumentDetailsProto.newBuilder().setType("csv")
				.addAllDatas(datasForCSV2).build();

		dataProcessor.addData(csvPayloadData2);

		// Add two more records to XML File

		List<DataModelProto> datasForXML2 = new ArrayList<>();
		datasForXML2.add(createTestDataModel("TestUser6", "1987-12-07", "60000"));
		datasForXML2.add(createTestDataModel("TestUser7", "1967-04-11", "85000"));

		DocumentDetailsProto xmlPayloadData2 = DocumentDetailsProto.newBuilder().setType("xMl")
				.addAllDatas(datasForXML2).build();

		dataProcessor.addData(xmlPayloadData2);

		// Read all Data and verify

		Documents documents2 = documentService.getAllData();

		Assert.assertEquals(3, documents2.getCsvDocument().getSize());
		Assert.assertEquals(5, documents2.getXmlDocument().getSize());

		// Update records in CSV

		DataModel csvDataToBeUpdated = documents2.getCsvDocument().getDatas().get(2);
		String csvIdToBeUpdated = csvDataToBeUpdated.getId();
		String csvNameUpdated = "TestUser3Updated";
		String csvSalaryUpdated = "90000";
		String csvDobUpdated = "1995-07-12";

		DataModelProto csvProtoToUpdate = DataModelProto.newBuilder().setId(csvIdToBeUpdated).setName(csvNameUpdated)
				.setSalary(csvSalaryUpdated).setDob(csvDobUpdated).build();
		DocumentDetailsProto csvPayloadData3 = DocumentDetailsProto.newBuilder().setType("csv")
				.addDatas(csvProtoToUpdate).build();

		dataProcessor.updateData(csvPayloadData3);

		// Update records in XML

		DataModel xmlDataToBeUpdated = documents2.getXmlDocument().getDatas().get(3);
		String xmlIdToBeUpdated = xmlDataToBeUpdated.getId();
		String xmlNameUpdated = "TestUser4Updated";
		String xmlSalaryUpdated = "46000";
		String xmlDobUpdated = "1957-10-12";

		DataModelProto xmlProtoToUpdate = DataModelProto.newBuilder().setId(xmlIdToBeUpdated).setName(xmlNameUpdated)
				.setSalary(xmlSalaryUpdated).setDob(xmlDobUpdated).build();
		DocumentDetailsProto xmlPayloadData3 = DocumentDetailsProto.newBuilder().setType("xml")
				.addDatas(xmlProtoToUpdate).build();

		dataProcessor.updateData(xmlPayloadData3);

		// Read all Data and verify

		Documents updatedDocuments = documentService.getAllData();

		updatedDocuments.getCsvDocument().getDatas().forEach(csvData -> {

			if (csvIdToBeUpdated.equals(csvData.getId())) {
				Assert.assertEquals(csvNameUpdated, csvData.getName());
				Assert.assertEquals(csvSalaryUpdated, csvData.getSalary());
				Assert.assertEquals(csvDobUpdated, csvData.getDob());
			}

		});

		updatedDocuments.getXmlDocument().getDatas().forEach(xmlData -> {

			if (xmlIdToBeUpdated.equals(xmlData.getId())) {
				Assert.assertEquals(xmlNameUpdated, xmlData.getName());
				Assert.assertEquals(xmlSalaryUpdated, xmlData.getSalary());
				Assert.assertEquals(xmlDobUpdated, xmlData.getDob());
			}

		});

	}

	/**
	 * @param salary
	 * @param dob
	 * @param name
	 * @return
	 */
	private DataModelProto createTestDataModel(String name, String dob, String salary) {
		return DataModelProto.newBuilder().setDob(dob).setName(name).setSalary(salary).build();
	}

}
