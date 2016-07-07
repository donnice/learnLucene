package lia.searching;

import org.apache.lucene.util.Version;
import org.apache.lucene.store.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.document.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import junit.framework.TestCase;

public class NearRealTimeTest extends TestCase {
	public void testNearRealTime() throws Exception {
		Directory dir = new RAMDirectory();
		IndexWriter writer = new IndexWriter(dir,new StandardAnalyzer(Version.LUCENE_30),
									IndexWriter.MaxFieldLength.UNLIMITED);
		
		for(int i = 0; i < 10; i++){
			Document doc = new Document();
			doc.add(new Field("id",""+i,
								Field.Store.NO, 
								Field.Index.NOT_ANALYZED_NO_NORMS));
			doc.add(new Field("text","aaa",
								Field.Store.NO,
								Field.Index.ANALYZED));
			writer.addDocument(doc);
		}
		IndexReader reader = writer.getReader();
		IndexSearcher searcher = new IndexSearcher(reader);
		
		Query query = new TermQuery(new Term("text","aaa"));
		TopDocs docs = searcher.search(query,1);
		assertEquals(10,docs.totalHits);
		
		writer.deleteDocuments(new Term("id","7"));
		
		Document doc = new Document();
		doc.add(new Field("id","11",
						   Field.Store.NO,
						   Field.Index.NOT_ANALYZED_NO_NORMS));
		doc.add(new Field("text","bbb",
						  Field.Store.NO,
						  Field.Index.ANALYZED));
		writer.addDocument(doc);
		
		IndexReader newReader = reader.reopen();
		assertFalse(reader == newReader);
		reader.close();
		searcher = new IndexSearcher(newReader);
		
		TopDocs hits = searcher.search(query, 10);
		assertEquals(9,hits.totalHits);
		
		query = new TermQuery(new Term("text","bbb"));
		hits = searcher.search(query,1);
		assertEquals(1,hits.totalHits);
		
		newReader.close();
		writer.close();
	}
}
