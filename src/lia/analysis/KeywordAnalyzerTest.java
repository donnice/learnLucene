package lia.analysis;


import junit.framework.TestCase;

import lia.common.TestUtil;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;

public class KeywordAnalyzerTest extends TestCase{
	
	private IndexSearcher searcher;
	
	public void setUp() throws Exception{
		Directory directory = new RAMDirectory();
		
		IndexWriter writer = new IndexWriter(directory,
									new SimpleAnalyzer(),
									IndexWriter.MaxFieldLength.UNLIMITED);
		Document doc = new Document();
		doc.add(new Field("partum","Q36",
						   Field.Store.NO,
						   Field.Index.NOT_ANALYZED_NO_NORMS));
		
	}
}
