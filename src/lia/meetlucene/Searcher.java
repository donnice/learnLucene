package lia.meetlucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

public class Searcher {
	
	public static void main(String[] args) throws Exception{
		String indexDir = "/Users/shujiec/Documents/Index";
		String q = args[0];
		
//		if(!indexDir.exists() || !indexDir.isDirectory()){
//			throw new Exception(indexDir +
//				"does not exist or is not a directory.");
//		}
		
		search(indexDir,q);
	}
	
	public static void search(String indexDir, String q) throws Exception{
		Directory fsDir = FSDirectory.open(new File(indexDir));
		IndexSearcher is = new IndexSearcher(fsDir);
		
		QueryParser parser = new QueryParser(Version.LUCENE_30,"contents",
				new StandardAnalyzer(
				  Version.LUCENE_30));
		Query query = parser.parse(q);
		
	}
}
