/**
 * 
 */
package com.frost.documentservice.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.frost.documentservice.client.DocumentClient;
import com.frost.documentservice.model.DataModel;
import com.frost.documentservice.model.DocumentDetails;
import com.frost.documentservice.model.Documents;
import com.frost.documentservice.protobuf.DocumentProtos.DocumentDetailsProto;
import com.frost.documentservice.publisher.DocumentPublisher;
import com.google.gson.Gson;

/**
 * @author jobin
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DocumentService.class, DocumentPublisher.class, DataTransformationService.class })
@TestPropertySource(properties = { "kafka.topic.create=test-create", "kafka.topic.update=test-update" })
public class DocumentServiceTest {

	@Autowired
	private DocumentService service;

	@MockBean
	private DocumentClient client;

	@MockBean
	private Producer<String, DocumentDetailsProto> producer;

	@MockBean
	private CryptoService cryptoService;

	@Test
	public void testAddData() {

		ArgumentCaptor<ProducerRecord> savedCaptor = ArgumentCaptor.forClass(ProducerRecord.class);

		Mockito.when(cryptoService.encrypt(ArgumentMatchers.anyString())).thenAnswer(arg -> arg.getArgument(0));

		List<DataModel> datas = new ArrayList<>();
		datas.add(createData("1", "1993-01-01", "TestUser1", "10000"));
		datas.add(createData("2", "1997-01-01", "TestUser2", "20000"));
		datas.add(createData("3", "1999-01-01", "TestUser3", "50000"));

		service.addDataToDocument("XML", datas);

		Mockito.verify(producer, Mockito.times(1)).send(savedCaptor.capture());

		MatcherAssert.assertThat(savedCaptor.getValue(), Matchers.instanceOf(ProducerRecord.class));
		ProducerRecord record = savedCaptor.getValue();
		Assert.assertEquals("test-create", record.topic());
		MatcherAssert.assertThat(record.value(), Matchers.instanceOf(DocumentDetailsProto.class));
		DocumentDetailsProto protoData = (DocumentDetailsProto) record.value();

		Assert.assertEquals(3, protoData.getDatasList().size());

		protoData.getDatasList().forEach(data -> {

			String id = data.getId();

			if (("1").equals(id)) {
				Assert.assertEquals("1993-01-01", data.getDob());
				Assert.assertEquals("10000", data.getSalary());
				Assert.assertEquals("TestUser1", data.getName());
			} else if (("2").equals(id)) {
				Assert.assertEquals("1997-01-01", data.getDob());
				Assert.assertEquals("20000", data.getSalary());
				Assert.assertEquals("TestUser2", data.getName());
			} else if (("3").equals(id)) {
				Assert.assertEquals("1999-01-01", data.getDob());
				Assert.assertEquals("50000", data.getSalary());
				Assert.assertEquals("TestUser3", data.getName());
			} else {
				Assert.fail("Invalid UserID Found : " + id);
			}

		});

	}

	@Test
	public void testUpdateData() {

		ArgumentCaptor<ProducerRecord> savedCaptor = ArgumentCaptor.forClass(ProducerRecord.class);

		Mockito.when(cryptoService.encrypt(ArgumentMatchers.anyString())).thenAnswer(arg -> arg.getArgument(0));

		List<DataModel> datas = new ArrayList<>();
		datas.add(createData("1", "1993-01-01", "TestUser1", "10000"));
		datas.add(createData("2", "1997-01-01", "TestUser2", "20000"));
		datas.add(createData("3", "1999-01-01", "TestUser3", "50000"));

		service.updateDocument("CSV", datas);

		Mockito.verify(producer, Mockito.times(1)).send(savedCaptor.capture());

		MatcherAssert.assertThat(savedCaptor.getValue(), Matchers.instanceOf(ProducerRecord.class));
		ProducerRecord record = savedCaptor.getValue();
		Assert.assertEquals("test-update", record.topic());
		MatcherAssert.assertThat(record.value(), Matchers.instanceOf(DocumentDetailsProto.class));
		DocumentDetailsProto protoData = (DocumentDetailsProto) record.value();

		Assert.assertEquals(3, protoData.getDatasList().size());

		protoData.getDatasList().forEach(data -> {

			String id = data.getId();

			if (("1").equals(id)) {
				Assert.assertEquals("1993-01-01", data.getDob());
				Assert.assertEquals("10000", data.getSalary());
				Assert.assertEquals("TestUser1", data.getName());
			} else if (("2").equals(id)) {
				Assert.assertEquals("1997-01-01", data.getDob());
				Assert.assertEquals("20000", data.getSalary());
				Assert.assertEquals("TestUser2", data.getName());
			} else if (("3").equals(id)) {
				Assert.assertEquals("1999-01-01", data.getDob());
				Assert.assertEquals("50000", data.getSalary());
				Assert.assertEquals("TestUser3", data.getName());
			} else {
				Assert.fail("Invalid UserID Found : " + id);
			}

		});

	}

	@Test
	public void testGetAllData() {

		Mockito.when(cryptoService.decrypt(ArgumentMatchers.anyString())).thenAnswer(arg -> arg.getArgument(0));

		Gson gson = new Gson();
		String fileData = readData("src/test/resources/get-data-sample.json");
		Documents documents = gson.fromJson(fileData, Documents.class);

		Mockito.when(client.getAllData()).thenReturn(documents);

		Documents responseDocuments = service.getAllData();

		Assert.assertNotNull(responseDocuments);

		DocumentDetails csvDocument = responseDocuments.getCsvDocument();
		DocumentDetails xmlDocument = responseDocuments.getXmlDocument();

		Assert.assertNotNull(csvDocument);
		Assert.assertNotNull(xmlDocument);
		Assert.assertEquals(2, csvDocument.getSize());
		Assert.assertEquals(2, csvDocument.getDatas().size());
		Assert.assertEquals(5, xmlDocument.getSize());
		Assert.assertEquals(5, xmlDocument.getDatas().size());
		Assert.assertEquals(fileData, gson.toJson(responseDocuments));

	}

	/**
	 * @param dob
	 * @param name
	 * @param salary
	 * @return
	 */
	private DataModel createData(String id, String dob, String name, String salary) {
		DataModel data = new DataModel();
		data.setId(id);
		data.setDob(dob);
		data.setName(name);
		data.setSalary(salary);
		return data;
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
