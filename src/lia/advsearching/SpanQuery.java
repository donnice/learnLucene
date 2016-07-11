package lia.advsearching;


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
import org.apache.lucene.search.spans.SpanFirstQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanNotQuery;
import org.apache.lucene.search.spans.SpanOrQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.spans.Spans;
import org.apache.lucene.store.RAMDirectory;

//  A span in this context is a starting and ending position in a field.
//  The individual spans, perhaps more than one per field, are tracked

public class SpanQuery extends TestCase{
	private RAMDirectory directory;
	private IndexSearcher searcher;
	private IndexReader reader;
	
	private SpanTermQuery quick;
	private SpanTermQuery brown;
	private SpanTermQuery red;
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
		
	}
	
}
