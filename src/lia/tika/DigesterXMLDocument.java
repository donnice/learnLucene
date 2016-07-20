package lia.tika;

// Digester is a layer on top of the SAX xml parser API to make it easier to process xml input. In particular, 
// digester makes it easy to create and initialise a tree of objects based 
// on an xml input file.

// page 270

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

public class DigesterXMLDocument {
	
	private Digester dig;
	private static Document doc;
	
	public DigesterXMLDocument() {
		dig = new Digester();
		dig.setValidating(false);
		
		dig.addObjectCreate("address-book", DigesterXMLDocument.class);
		dig.addObjectCreate("address-book/contact", Contact.class);
		
		dig.addSetProperties("address-book/contact","type","type");
		
		dig.addCallMethod("address-book/contact/name", "setName",0);
		dig.addCallMethod("address-book/contact/city", "setCity",0);
		dig.addCallMethod("address-book/contact/address", "setAddress",0);
		dig.addCallMethod("address-book/contact/province", "setProvince",0);
		dig.addCallMethod("address-book/contact/postalcode", "setPostalCode",0);
		dig.addCallMethod("address-book/contact/country", "setCountry",0);
		dig.addCallMethod("address-book/contact/telephone", "setTelephone",0);
		
		dig.addSetNext("address-book/contact", "populateDocument");
	}
	
	public synchronized Document getDocument(InputStream is)
		throws DocumentHandlerException {
		try {
			dig.parse(is);
		} catch (IOException e) {
			throw new DocumentHandlerException("Cannot parse XML document",e);
		} catch (SAXException e) {
			throw new DocumentHandlerException("Cannot parse XML document",e);
		}
		
		return doc;
	}
	
	public void populateDocument(Contact contact) {
		doc = new Document();
		doc.add(new Field("type",contact.getType(),Field.Store.YES,Field.Index.NOT_ANALYZED));
		doc.add(new Field("name",contact.getType(),Field.Store.YES,Field.Index.NOT_ANALYZED));
		doc.add(new Field("address",contact.getType(),Field.Store.YES,Field.Index.NOT_ANALYZED));
		doc.add(new Field("city",contact.getType(),Field.Store.YES,Field.Index.NOT_ANALYZED));
		doc.add(new Field("type",contact.getType(),Field.Store.YES,Field.Index.NOT_ANALYZED));
		doc.add(new Field("province",contact.getType(),Field.Store.YES,Field.Index.NOT_ANALYZED));
		doc.add(new Field("postalcode",contact.getType(),Field.Store.YES,Field.Index.NOT_ANALYZED));
		doc.add(new Field("country",contact.getType(),Field.Store.YES,Field.Index.NOT_ANALYZED));
		doc.add(new Field("telephone",contact.getType(),Field.Store.YES,Field.Index.NOT_ANALYZED));
		
	}
	
	public static class Contact {
		private String type;
		private String name;
		private String address;
		private String city;
		private String province;
		private String postalcode;
		private String country;
		private String telephone;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getProvince() {
			return province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getPostalcode() {
			return postalcode;
		}
		public void setPostalcode(String postalcode) {
			this.postalcode = postalcode;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public String getTelephone() {
			return telephone;
		}
		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}
		
	}
}
