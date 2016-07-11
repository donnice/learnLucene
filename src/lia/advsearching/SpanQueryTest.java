package lia.advsearching;


import java.io.IOException;
import java.io.StringReader;

import junit.framework.TestCase;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.SpanQueryFilter;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanFirstQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanNotQuery;
import org.apache.lucene.search.spans.SpanOrQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.spans.Spans;
import org.apache.lucene.store.RAMDirectory;

//  A span in this context is a starting and ending position in a field.
//  The individual spans, perhaps more than one per field, are tracked
//  Spans provide a proximity search feature to Lucene. 
//  They are used to find multiple terms near each other, without requiring the terms to appear in a specified order.
//  By itself, a SpanTermQuery matches documents just like TermQuery does, but it also keeps track of position of the same terms that appear within each document.

public class SpanQueryTest extends TestCase{
	private RAMDirectory directory;
	private IndexSearcher searcher;
	private IndexReader reader;
	
	private SpanTermQuery quick;
	private SpanTermQuery brown;
	private SpanTermQuery red;
	private SpanTermQuery fox;
	private SpanTermQuery lazy;
	private SpanTermQuery sleepy;
	private SpanTermQuery dog;
	private SpanTermQuery cat;
	private Analyzer analyzer;
	
	protected void setUp() throws Exception{
		directory = new RAMDirectory();
		
		analyzer = new WhitespaceAnalyzer();
		IndexWriter writer = new IndexWriter(directory,
											 analyzer,
											 IndexWriter.MaxFieldLength.UNLIMITED);
		Document doc = new Document();
		doc.add(new Field("f",
			"the quick brown fox jumps over the lazy dog",
			Field.Store.YES,Field.Index.ANALYZED));
		writer.addDocument(doc);
		
		doc = new Document();
		doc.add(new Field("f",
			"the quick red fox jumps over the sleepy cat",
			Field.Store.YES,Field.Index.ANALYZED));
		writer.addDocument(doc);
		
		writer.close();
		
		searcher = new IndexSearcher(directory);
		reader = searcher.getIndexReader();
		
		quick = new SpanTermQuery(new Term("f","quick"));
		brown = new SpanTermQuery(new Term("f","brown"));
		red = new SpanTermQuery(new Term("f","red"));
		fox = new SpanTermQuery(new Term("f","fox"));
		lazy = new SpanTermQuery(new Term("f","lazy"));
		sleepy = new SpanTermQuery(new Term("f","sleepy"));
		dog = new SpanTermQuery(new Term("f","dog"));
		cat = new SpanTermQuery(new Term("f","cat"));
	}
	
	private void assertOnlyBrownFox(Query query) throws Exception {
		TopDocs hits = searcher.search(query, 10);
		assertEquals(1,hits.totalHits);
		assertEquals("wrong doc",0,hits.scoreDocs[0].doc);
	}
	
	private void assertBothFoxes(Query query) throws Exception {
		TopDocs hits = searcher.search(query,10);
		assertEquals(2,hits.totalHits);
	}
	
	private void assertNoMatches(Query query) throws Exception {
		TopDocs hits = searcher.search(query, 10);
		assertEquals(0,hits.totalHits);
	}
	
	private void dumpSpans(SpanQuery query) throws IOException {
		Spans spans = query.getSpans(reader);
	    System.out.println(query+":");
	    int numSpans = 0;
	    
	    TopDocs hits = searcher.search(query, 10);
	    float[] scores = new float[2];
	    for(ScoreDoc sd:hits.scoreDocs){
	    	scores[sd.doc] = sd.score;
	    }
	    
	    while(spans.next()){
	    	numSpans++;
	    	
	    	int id = spans.doc();
	    	Document doc = reader.document(id);
	    	
	    	TokenStream stream = analyzer.tokenStream("contents", new StringReader(doc.get("f")));
	    	
	    	TermAttribute term = stream.addAttribute(TermAttribute.class);
	    	
	    	StringBuilder buffer = new StringBuilder();
	    	buffer.append("  ");
	    	int i = 0;
	    	while(stream.incrementToken()){
	    		if(i == spans.start()){
	    			buffer.append("<");
	    		}
	    		buffer.append(term.term()); // E Print < and > around span
	    		if(i + 1 == spans.end()){
	    			buffer.append(">");
	    		}
	    		buffer.append(" ");
	    		i++;
	    	}
	    	buffer.append("(").append(scores[id]).append(")");
	    	System.out.println(buffer);
	    }
	    
	    if(numSpans == 0){
	    	System.out.println("   No spans!");
	    }
	    System.out.println();
	}
	
	public void testSpanTermQuery() throws Exception {
		assertOnlyBrownFox(brown);
		dumpSpans(brown);
	}
	
	// Finding spans at the beginning of a field
	public void testSpanFirstQuery() throws Exception { 
		SpanFirstQuery sfq = new SpanFirstQuery(brown,2);
		assertNoMatches(sfq);
		
		dumpSpans(sfq);
		
		sfq = new SpanFirstQuery(brown,3);
		dumpSpans(sfq);
		assertOnlyBrownFox(sfq);
	}
	
	public void testSpanNearQuery() throws Exception{
		SpanQuery[] quick_brown_dog = 
			new SpanQuery[]{quick,brown,dog};
		SpanNearQuery snq = 
			new SpanNearQuery(quick_brown_dog,0,true);
		assertNoMatches(snq);
		dumpSpans(snq);
		
		snq = new SpanNearQuery(quick_brown_dog,4,true);
		assertNoMatches(snq);
		dumpSpans(snq);
		
		snq = new SpanNearQuery(quick_brown_dog,5,true);
		assertOnlyBrownFox(snq);
		dumpSpans(snq);
		
		snq = new SpanNearQuery(new SpanQuery[]{lazy,fox},3,false);// bool in order
		assertOnlyBrownFox(snq);
		dumpSpans(snq);
		
		PhraseQuery pq = new PhraseQuery();
		pq.add(new Term("f","lazy"));
		pq.add(new Term("f","fox"));
		pq.setSlop(4);
		assertNoMatches(pq);
		
		pq.setSlop(5);
		assertOnlyBrownFox(pq);
	}
	
	public void testSpanNotQuery() throws Exception {
		SpanNearQuery quick_fox = new SpanNearQuery(new SpanQuery[]{quick,fox},1,true);
		assertBothFoxes(quick_fox);
		dumpSpans(quick_fox);
		
		SpanNotQuery quick_fox_dog = new SpanNotQuery(quick_fox,brown);// exclude "dog
		assertBothFoxes(quick_fox_dog);
		dumpSpans(quick_fox_dog);
	}
	
	
}
