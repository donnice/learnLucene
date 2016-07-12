package lia.advsearching;

import junit.framework.TestCase;
import lia.common.TestUtil;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class SecurityFilterTest extends TestCase{
	
	private IndexSearcher searcher;
	
	protected void setUp() throws Exception {
		Directory directory = new RAMDirectory();
		IndexWriter writer = new IndexWriter(directory,
									new WhitespaceAnalyzer(),
							IndexWriter.MaxFieldLength.UNLIMITED);
		
		Document document = new Document();
		document.add(new Field("owner",
							   "elwood",
							   Field.Store.YES,
							   Field.Index.NOT_ANALYZED));
		document.add(new Field("keywords",
							   "elwood's sensitive info",
							   Field.Store.YES,
							   Field.Index.ANALYZED));
		writer.addDocument(document);
		
		document = new Document();
		document.add(new Field("owner",
							   "jake",
							   Field.Store.YES,
							   Field.Index.NOT_ANALYZED));
		document.add(new Field("keywords",
							   "jake's sensitive info",
							   Field.Store.YES,
							   Field.Index.ANALYZED));
		writer.addDocument(document);
		
		writer.close();
		searcher = new IndexSearcher(directory);
	}
	
	public void testSecurityFilter() throws Exception {
		TermQuery query = new TermQuery(
							new Term("keywords","info"));
		assertEquals("Both documents match",
					2, TestUtil.hitCount(searcher, query));
		
		Filter jakeFilter = new QueryWrapperFilter(
			new TermQuery(new Term("owner","jake")));
		
		TopDocs hits = searcher.search(query,jakeFilter,10);
		assertEquals(1,hits.totalHits);
		assertEquals("elwood is safe",
					 "jake's sensitive info",
				searcher.doc(hits.scoreDocs[0].doc).get("keywords"));
	}
}
