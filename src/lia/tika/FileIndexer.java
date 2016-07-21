package lia.tika;

//	page 295

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.document.Document;
import org.apache.lucene.ant.DocumentHandlerException;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.index.Term;

public class FileIndexer {
	protected FileHandler fileHandler;
	
	public FileIndexer(Properties props) throws IOException {
		fileHandler = new ExtensionFileHandler(props);
	}
	
	public void index(IndexWriter writer, File file) 
		throws DocumentHandlerException, IllegalAccessException {
		if(file.canRead()) {
			if(file.isDirectory()) {
				String[] files = file.list();
				if(files != null) {
					for(int i = 0; i < files.length; i++)
						index(writer,new File(file,files[i]));
					// Creates a new File instance from a parent abstract pathname and a child pathname string.
				}
			}
		}
		else {
			System.out.println("Indexing "+file);
			try {
				Document doc = fileHandler.getDocument(file);
				if(doc != null)
					writer.addDocument(doc);
				else
					System.err.println("Cannot handle"+file.getAbsolutePath());
			} catch(IOException e) {
				System.err.println("Cannot index!");
			}
		}
	}

}
