/**
 * 
 */
package com.frost.storageservice.writer;

import static org.joox.JOOX.$;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.joox.JOOX;
import org.joox.Match;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.frost.storageservice.exception.WriterException;
import com.frost.storageservice.model.DataModel;
import com.frost.storageservice.model.DocumentDetails;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jobin
 *
 */
@Slf4j
@Service("XML")
public class XMLDataWriter implements DataWriter {

	@Value("${storage.path}")
	private String storagePath;

	private Match xmlFile;
	private String root;
	private String data;
	private String dataXpathTemplate;
	private File file;

	@PostConstruct
	@Override
	public void initialize() {

		log.info("Initializing XMLDataWriter");
		initializeXMLFile();
		log.info("XMLDataWriter Ready");

	}

	/**
	 * 
	 */
	private void initializeXMLFile() {

		try {

			root = "<datas></datas>";
			data = "<data id=\"ID\" name=\"NAME\" dob=\"DOB\" salary=\"SALARY\"/>";
			dataXpathTemplate = ("/datas/data[@id='ID']");

			file = new File(getFilePath());

			if (file.exists()) {
				xmlFile = $(file);
			}

			initializeMetaData();
			save();

		} catch (Exception e) {
			throw new WriterException("Failed to initialize the XML Data file at " + getFilePath(), e);
		}

	}

	/**
	 * 
	 */
	private void initializeMetaData() {

		String count = getCurrentSize();

		if (count == null) {
			String metaData = "<data id=\"0\" size=\"0\"/>";
			$(xmlFile).append(metaData);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean write(DocumentDetails document) {

		log.info("Adding Data to XML!");

		initializeXMLFile();

		int count = Integer.valueOf(getCurrentSize());
		log.info("Current Count: {}", count);

		Gson gson = new Gson();

		for (DataModel data : document.getDatas()) {

			count++;
			data.setId(String.valueOf(count));

			Map<String, String> dataMap = gson.fromJson(gson.toJson(data), Map.class);

			writeOrUpdate(dataMap);

		}

		log.info("Updated Count: {}", count);
		updateLatestCount(count);
		save();

		return true;

	}

	@Override
	public boolean update(DocumentDetails document) {

		log.info("Updating Data in XML!");

		initializeXMLFile();
		writeOrUpdate(document);
		save();

		return true;
	}

	/**
	 * @param document
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void writeOrUpdate(DocumentDetails document) {

		Gson gson = new Gson();

		document.getDatas().forEach(data -> {

			Map<String, String> dataMap = gson.fromJson(gson.toJson(data), Map.class);

			writeOrUpdate(dataMap);
			save();

		});

	}

	@Override
	public DocumentDetails readAll() {

		log.info("Starting to read Data from XML!");

		initializeXMLFile();

		DocumentDetails documents = new DocumentDetails();
		documents.setType("XML");
		List<DataModel> datas = new ArrayList<>();
		int size = Integer.valueOf(getCurrentSize());

		for (int position = 1; position <= size; position++) {

			DataModel data = new DataModel();

			String id = String.valueOf(position);
			Match recordMatch = xmlFileMatch().xpath(dataXpath(id));

			data.setName(recordMatch.attr("name"));
			data.setDob(recordMatch.attr("dob"));
			data.setSalary(recordMatch.attr("salary"));
			data.setId(id);

			datas.add(data);
		}

		documents.setSize(size);
		documents.setDatas(datas);
		return documents;
	}

	/**
	 * @param dataMap
	 * 
	 */
	private void writeOrUpdate(Map<String, String> dataMap) {

		Match dbMatch = xmlFileMatch();
		String newdata = data;

		for (Entry<String, String> dataEntry : dataMap.entrySet()) {
			String key = dataEntry.getKey().toUpperCase();
			String value = dataEntry.getValue();
			newdata = newdata.replace(key, value);
		}

		String id = dataId(newdata);
		String dataXpath = dataXpath(id);
		Match data = $(dbMatch).xpath(dataXpath);

		if ($(data).isEmpty()) {
			$(dbMatch).append(newdata);
		} else {
			for (Entry<String, String> dataEntry : dataMap.entrySet()) {
				String key = dataEntry.getKey();
				String value = dataEntry.getValue();
				$(dbMatch).xpath(dataXpath).attr(key, value);
			}
		}

		log.info("Data id " + id + " added succesfully!");

	}

	/**
	 * 
	 * @return
	 */
	public synchronized boolean save() {

		try {
			FileUtils.writeStringToFile(file, xmlFileMatch().toString(), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 
	 * @return
	 */
	private Match xmlFileMatch() {

		if (xmlFile == null) {
			xmlFile = xmlStrToMatch(root, "UTF-8");
		}

		return xmlFile;
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	private String dataId(String data) {
		String id = xmlStrToMatch(data, "UTF-8").attr("id");
		return id;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	private String dataXpath(String id) {
		String dataXpath = dataXpathTemplate.replace("ID", id);
		return dataXpath;
	}

	/**
	 * @param count
	 */
	private void updateLatestCount(int count) {
		Match dbMatch = xmlFileMatch();
		$(dbMatch).xpath("//data[@id='0']").attr("size", String.valueOf(count));
	}

	/**
	 * @return
	 */
	private String getCurrentSize() {
		return xmlFileMatch().xpath("//data[@id='0']").attr("size");
	}

	/**
	 * 
	 * @param xmlString
	 * @param encoding
	 * @return
	 */
	private static Match xmlStrToMatch(String xmlString, String encoding) {

		Match match = null;

		try {
			match = $(JOOX.builder().parse(new ByteArrayInputStream(xmlString.getBytes(encoding))));
		} catch (Exception e) {
			throw new WriterException("Failed to match generate XML Match!", e);
		}

		return match;
	}

	@Override
	public String getFilePath() {
		return storagePath + "/datafile.xml";
	}

}
