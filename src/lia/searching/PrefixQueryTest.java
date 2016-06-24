package lia.searching;

import junit.framework.TestCase;
import lia.common.TestUtil;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

// From chapter 3
public class PrefixQueryTest extends TestCase {
	
	Directory dir = new RAMDirectory();
	
	public void indexSingleFieldDocs(Field[] fields) throws Exception {
		IndexWriter writer = new IndexWriter(dir,
				new WhitespaceAnalyzer(), IndexWriter.MaxFieldLength.UNLIMITED);
		for(Field f: fields){
			Document doc = new Document();
			doc.add(f);
			writer.addDocument(doc);
		}
		writer.optimize();
		writer.close();
	}
	
	public void testPrefix() throws Exception {
//    Directory dir = TestUtil.getBookIndexDirectory();
    
    indexSingleFieldDocs(new Field[]{
    		new Field("category","/technology/computers/programming",Field.Store.YES,Field.Index.ANALYZED),
    		new Field("category","/technology/computers/programmingplay and else",Field.Store.YES,Field.Index.ANALYZED)
    
    });
    
    IndexSearcher searcher = new IndexSearcher(dir);

    Term term = new Term("category",
    					 "/technology/computers/programming");
    PrefixQuery query = new PrefixQuery(term);
    TermQuery termQuery = new TermQuery(term);
    
    TopDocs matches = searcher.search(query, 10);
    int programmingAndBelow = matches.totalHits;
    
    matches = searcher.search(termQuery, 10);
    int justProgramming = matches.totalHits;
    
    assertTrue(programmingAndBelow >= justProgramming);
    searcher.close();
    dir.close();
    
  }
}