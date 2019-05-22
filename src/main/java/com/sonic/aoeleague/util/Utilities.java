package com.sonic.aoeleague.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;

public class Utilities {
	public static String dateToString(XMLGregorianCalendar xmlGregorianCalendar, SimpleDateFormat dateFormat) {
		if (xmlGregorianCalendar != null) {
			return dateFormat.format(xmlGregorianCalendar.toGregorianCalendar().getTime());
		}
		return null;
	}
	
	public static XMLGregorianCalendar fromStringToXMLCalendar(String text, SimpleDateFormat dateFormat) {
		if (text != null) {
			try {
				Date date = dateFormat.parse(text);
				GregorianCalendar gregorianCalendar = new GregorianCalendar(Constants.TIME_ZONE);
				gregorianCalendar.setTime(date);
				XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
				return xmlGregorianCalendar;
			} catch (ParseException | DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String getCurrentTimeStr(SimpleDateFormat dateFormat) {
		Calendar calendar = Calendar.getInstance(Constants.TIME_ZONE);
		Date date = calendar.getTime();
		return dateFormat.format(date);
	}
	
	public static XMLGregorianCalendar getCurrentTime() {
		GregorianCalendar gregorianCalendar = new GregorianCalendar(Constants.TIME_ZONE);
		XMLGregorianCalendar xmlGregorianCalendar = null;
		try {
			xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xmlGregorianCalendar;
	}
	
	public static Calendar toCalendar(XMLGregorianCalendar xmlGregorianCalendar) {
		return xmlGregorianCalendar.toGregorianCalendar();
	}
	public static List<String> toListString(String input, String delimiter) {
		if (input != null) {
			String [] splitter = input.split(delimiter);
			if (splitter != null) {
				List<String> returnSet = new ArrayList<String>();
				for (String value : splitter) {
					value = value.trim();
					if (StringUtils.isNotEmpty(value)) {
						returnSet.add(value);
					}
				}
				return returnSet;
			}
		}
		return null;
	}
	
	public static boolean isEmpty(Collection<?> c) {
		return c == null || c.isEmpty();
	}
	
	public static int [] getScore(String result) {
		String[] resultSplitter = result.split("-");
		if (resultSplitter.length != 2)
			return null;

		int[] score = new int[2];
		try {
			score[0] = Integer.parseInt(resultSplitter[0].trim());
			score[1] = Integer.parseInt(resultSplitter[1].trim());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return score;
	}
}
