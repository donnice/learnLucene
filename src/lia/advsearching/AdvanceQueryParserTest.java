package lia.advsearching;

import junit.framework.TestCase;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.util.Version;

public class AdvanceQueryParserTest extends TestCase{
	private Analyzer analyzer = new WhitespaceAnalyzer();
	
	public void testCustomQueryParser() {
		CustomQueryParser parser = 
			new CustomQueryParser(Version.LUCENE_30,"field",analyzer);
		
		try {
			parser.parse("a?t");
			fail("Wildcard queries should not be allowed");
		} catch (ParseException expected) {
			
		}
		
		try {
			parser.parse("xunit~");
			fail("Fuzzy queries should not be allowed");
		} catch (ParseException expected) {}
	}
	
	public void testPhraseQuery() throws Exception {
		CustomQueryParser parser = new CustomQueryParser(
				Version.LUCENE_30,"field",analyzer);
		Query query = parser.parse("singleTerm");
		assertTrue("TermQuery",query instanceof TermQuery);
		
		query = parser.parse("\"a phrase\"");
		assertTrue("SpanNearQuery",query instanceof SpanNearQuery);
	}
}
