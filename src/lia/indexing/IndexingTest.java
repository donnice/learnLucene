package lia.indexing;

import junit.framework.*;

import lia.common.*;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.index.Term;

import java.io.IOException;


public class IndexingTest extends TestCase{
	protected String[] ids = {"1","2"};
	protected String[] unindexed = {"Netherlands","Italy"};
	protected String[] unstored = {"Amsterdam has lots of bridges",
			"Venice has lots of canals"};
	protected String[] text = {"Amsterdam","Venice"};
	protected Directory dir;
	
	protected void setUp() throws IOException{	// run before test
		dir = new RAMDirectory();
		
		IndexWriter writer = getWriter();
		
		for(int i = 0; i < ids.length; i++){
			Document doc = new Document();
			doc.add(new Field("id",ids[i],
						Field.Store.YES,
						Field.Index.NO));
			doc.add(new Field("country",unindexed[i],
					Field.Store.YES,
					Field.Index.NOT_ANALYZED));
			doc.add(new Field("contents",unstored[i],
					Field.Store.NO,
					Field.Index.ANALYZED));
			doc.add(new Field("city",text[i],
					Field.Store.YES,
					Field.Index.ANALYZED));
			writer.addDocument(doc);
		}
		writer.close();
	}
	
	private IndexWriter getWriter() throws IOException {
		return new IndexWriter(dir, new WhitespaceAnalyzer(),
				IndexWriter.MaxFieldLength.UNLIMITED);
	}
	
	protected int getHitCount(String fieldName, String searchString)
		throws IOException{
		IndexSearcher searcher = new IndexSearcher(dir);	// new searcher
		Term t = new Term(fieldName, searchString);
		Query query = new TermQuery(t);
		int hitCount = TestUtil.hitCount(searcher,query);
		searcher.close();
		return hitCount;
	}
	
	public void testIndexWriter() throws IOException {
		IndexWriter writer = getWriter();
		assertEquals(ids.length, writer.numDocs());
		writer.close();
	}
	
	public void testIndexReader() throws IOException {
		IndexReader reader = IndexReader.open(dir);
		assertEquals(ids.length, reader.maxDoc());
		assertEquals(ids.length, reader.numDocs());
		reader.close();
	}
	
	public void testDeleteBeforeOptimize() throws IOException {
		IndexWriter writer = getWriter();
		assertEquals(2,writer.numDocs());
		writer.deleteDocuments(new Term("id","1"));
		writer.commit();
		assertTrue(writer.hasDeletions());
		assertEquals(2,writer.maxDoc());	// deleted docsa are also included
		assertEquals(1,writer.numDocs());
		writer.close();
	}
	
	public void testDeleteAfterOptimize() throws IOException {
		IndexWriter writer = getWriter();
		assertEquals(2,writer.numDocs());
		writer.deleteDocuments(new Term("id","1")); 	// deleteFirst
		writer.optimize();
		writer.commit();
		assertFalse(writer.hasDeletions());
		assertEquals(1,writer.maxDoc());	// deleted docs are also included
		assertEquals(1,writer.numDocs());
		writer.close();
	}
	
	public void testUpdate() throws IOException {
		
		assertEquals(1,getHitCount("city","Amsterdam"));
		
		IndexWriter writer = getWriter();
		
		Document doc = new Document();	//  Store the original field value in the index.
		doc.add(new Field("id","1",
						  Field.Store.YES,
						  Field.Index.NOT_ANALYZED));
		doc.add(new Field("country","Netherlands",
						  Field.Store.YES,
						  Field.Index.NO));
		doc.add(new Field("contents",
						  "Den Haag has a lot of museums but bridges",
						  Field.Store.NO,
						  Field.Index.ANALYZED));
		doc.add(new Field("city","Den Haag",
						  Field.Store.YES,
						  Field.Index.ANALYZED));
		writer.updateDocument(new Term("id","1"), doc);
		
		writer.close();
		
		assertEquals(0,getHitCount("city","Amsterdam"));
		assertEquals(1,getHitCount("city","Haag"));
	}
	
	public void testMaxFieldLength() throws IOException {
		
		assertEquals(1,getHitCount("contents","bridges"));
		
		IndexWriter writer = new IndexWriter(dir,new WhitespaceAnalyzer(),
									new IndexWriter.MaxFieldLength(1));
		Document doc = new Document();
		doc.add(new Field("contents",
						  "these bridges cannot be found",
						  Field.Store.NO,
						  Field.Index.ANALYZED));
		writer.addDocument(doc);
		writer.close();
		
		assertEquals(1,getHitCount("contents","bridges"));
	}
}
