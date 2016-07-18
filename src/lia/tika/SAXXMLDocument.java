package lia.tika;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SAXXMLDocument extends DefaultHandler{
	
	private StringBuilder elementBuffer = new StringBuilder();
	private Map<String,String> attributeMap = new HashMap<String,String>();
	
	private Document doc;
	
	public Document getDocument(InputStream is)
		throws DocumentHandlerException {
		
		return doc;
	}
}
