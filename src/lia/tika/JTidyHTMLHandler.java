package lia.tika;

import java.io.InputStream;


import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.w3c.tidy.Tidy;
import org.xml.sax.AttributeList;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.Node;




public class JTidyHTMLHandler implements DocumentHandler{
	
	public Document getDocument(InputStream is) throws DocumentHandlerException {
		
		Tidy tidy = new Tidy();
		tidy.setQuiet(true); // no 'Parsing X', guessed DTD or summary
		tidy.setShowWarnings(false);
		org.w3c.dom.Document root = tidy.parseDOM(is, null);
		
		Element rawDoc = root.getDocumentElement();
		
		Document doc = new Document();
		
		String title = getTitle(rawDoc);
		String body = getBody(rawDoc);
		
		if((title!=null) && (!title.equals("")))
			doc.add(new Field("title",title,Field.Store.YES,Field.Index.ANALYZED));
		if((body!=null) && (!body.equals("")))
			doc.add(new Field("body",body,Field.Store.YES,Field.Index.ANALYZED));
		
		return doc;
	}
	
	protected String getTitle(Element rawDoc) {
		if(rawDoc == null)
			return null;
		String title = "";
		
		NodeList children = rawDoc.getElementsByTagName("title");
		if(children.getLength() > 0) {
			Element titleElement = ((Element)children.item(0));
			Text text = (Text)titleElement.getFirstChild();
			if(text!=null)
				title = text.getData();
		}
		
		return title;
	}
	
	protected String getBody(Element rawDoc) {
		if(rawDoc == null)
			return null;
		String body = "";
		NodeList children = rawDoc.getElementsByTagName("body");
		if(children.getLength() > 0) {
			body = getText(children.item(0));
		}
		return body;
				
	}
	
	protected String getText(Node node) {
		NodeList children = node.getChildNodes();
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < children.getLength();i++) {
			Node child = children.item(i);
			switch(child.getNodeType()) {
				case Node.ELEMENT_NODE:
					sb.append(getText(child));
					sb.append(" ");
					break;
				case Node.TEXT_NODE:
					sb.append(((Text)child).getData());
					break;
			}
		}
		return sb.toString();
	}
	

	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endElement(String arg0) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processingInstruction(String arg0, String arg1)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDocumentLocator(Locator arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startElement(String arg0, AttributeList arg1)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}
	
}
