package lia.tika;

import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.ant.DocumentHandlerException;

//Creates a Lucene Document from a File.abstract
//This method can return <code>null</code>

public interface FileHandler {
	
	Document getDocument(File file) throws DocumentHandlerException, IllegalAccessException;
}
