package org.fao.aoscs.model.semanticturkey.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLUtil {

	static DocumentBuilderFactory domBuilderFactory;

	static {
		 initialize();
	}
	
	public static void initialize()
	{
		domBuilderFactory = DocumentBuilderFactory.newInstance();
	}
	
	public static synchronized Document inputStream2XML(InputStream streamedXml) throws SAXException, IOException, ParserConfigurationException {
		synchronized (domBuilderFactory)
		{
			DocumentBuilder builder = domBuilderFactory.newDocumentBuilder();
			return builder.parse(streamedXml);
		}
		
	}
}
