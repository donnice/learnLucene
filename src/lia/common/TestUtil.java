package lia.common;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Filter;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.Directory;

import java.io.*;

public class TestUtil {
	public static boolean hitsIncludeTitle(IndexSearcher searcher,
			TopDocs hits, String title) throws IOException {
		for(ScoreDoc match:hits.scoreDocs){
			Document doc = searcher.doc(match.doc);
			if(title.equals(doc.get("title"))){
				return true;
			}
		}
		System.out.println("title '"+title+"' not found!");
		return false;
	}
	
	
	
	
}
