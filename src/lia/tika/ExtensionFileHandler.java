package lia.tika;

import java.io.*;
import java.util.*;

import org.apache.lucene.document.*;
import org.apache.lucene.ant.*;

import org.apache.lucene.ant.DocumentHandlerException;

public class ExtensionFileHandler implements FileHandler{
	private Properties handlerProps;
	
	public ExtensionFileHandler(Properties props) throws IOException {
		handlerProps = props;
	}

	@Override
	public Document getDocument(File file) throws DocumentHandlerException, IllegalAccessException {
		// TODO Auto-generated method stub
		Document doc = null;
		String name = file.getName();
		int dotIndex = name.indexOf(".");
		if((dotIndex > 0) && (dotIndex < name.length())) {
			String ext = name.substring(dotIndex+1,name.length());
			String handlerClassName = handlerProps.getProperty(ext);
			
			try {
				Class handlerClass = Class.forName(handlerClassName);
				DocumentHandler handler = (DocumentHandler)handlerClass.newInstance();
				return handler.getDocument(file);
			} catch(ClassNotFoundException e) {
				
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentHandlerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
}
