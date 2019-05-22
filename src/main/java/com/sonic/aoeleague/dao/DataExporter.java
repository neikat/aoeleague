package com.sonic.aoeleague.dao;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class DataExporter {
	public static void exportToXMl(Object object, String path, Class clazz) {
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(object, new File(path));
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Convert to XML errror: "
					+ ex.getMessage());
		}
	}
}
