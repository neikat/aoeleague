package com.sonic.aoeleague.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;

public class DataImporter {
	public static Object importXml(String filePath, Class clazz) {
		Object ret = null;

		try {
			InputStream is = new FileInputStream(new File(filePath));
			JAXBContext context = JAXBContext.newInstance(clazz);
			ret = context.createUnmarshaller().unmarshal(is);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Load " + filePath + " failed: " + ex.getMessage());
		}
		return ret;
	}
}
