package lia.advsearching;

// page 243

import lia.common.TestUtil;
import junit.framework.TestCase;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.Query;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.util.Version;

import java.util.Locale;

public class NumericQueryParserTest extends TestCase{
	private Analyzer analyzer;
	private IndexSearcher searcher;
	private Directory dir;
	
	protected void setUp() throws Exception {
		analyzer = new WhitespaceAnalyzer();
		dir = TestUtil.getBookIndexDirectory();
		searcher = new IndexSearcher(dir,true);
	}
	
	protected void tearDown() throws Exception {
		searcher.close();
		dir.close();
	}
	
	static class NumericRangeQueryParser extends QueryParser {
		public NumericRangeQueryParser(Version matchVersion,
									   String field, Analyzer a ) {
			super(matchVersion,field,a);
		}
		
		public Query getRangeQuery(String field, 
				String part1, String part2, boolean inclusive) throws ParseException{
			TermRangeQuery query = (TermRangeQuery)super.getRangeQuery(field,part1,part2,inclusive);
			
			if("price".equals(field)){
				return NumericRangeQuery.newDoubleRange("price", Double.parseDouble(query.getLowerTerm()),
						Double.parseDouble(query.getUpperTerm()), query.includesLower(), query.includesUpper());
			} else
				return query;
		}
		
	}
	
	public void testNumericRangeQuery() throws Exception {
		String expression = "price:[10 TO 20]";
		
		QueryParser parser = new NumericRangeQueryParser(Version.LUCENE_30,
														"subject",analyzer);
		Query query = parser.parse(expression);
		System.out.println(expression+" parsed to "+query);
	}
	
	public static class NumericDateRangeQueryParser extends QueryParser {
		public NumericDateRangeQueryParser(Version matchVersion,String field, Analyzer a){
			super(matchVersion,field,a);
		}
		
		public Query getRangeQuery(String field, 
				String part1, String part2, boolean inclusive) throws ParseException{
			TermRangeQuery query = (TermRangeQuery)super.getRangeQuery(field,part1,part2,inclusive);
			
			if("pubmonth".equals(field)){
				return NumericRangeQuery.newIntRange("pubmonth", 
						Integer.parseInt(query.getLowerTerm()), 
						Integer.parseInt(query.getUpperTerm()), 
						query.includesLower(), query.includesUpper());
			} else {
				return query;
			}
		}
	}
	
	public void testDefaultDateRangeQuery() throws Exception {
		QueryParser parser = new QueryParser(Version.LUCENE_30,
											 "subject",analyzer);
		Query query = parser.parse("pubmonth:[1/1/04 TO 12/31/04]");
		System.out.println("default date parsing:"+query);
	}
	
	public void testDateRangeQuery() throws Exception {
		String expression = "pubmonth:[01/01/2010 TO 06/01/2010]";
		
		QueryParser parser = new NumericDateRangeQueryParser(Version.LUCENE_30,
															 "subject",analyzer);
		
		parser.setDateResolution("pubmonth",DateTools.Resolution.MONTH);
		parser.setLocale(Locale.US);
		
		Query query = parser.parse(expression);
		System.out.println(expression+" parsed to "+query);
		
		TopDocs matches = searcher.search(query,10);
		assertTrue("expectiong at least one result!",matches.totalHits > 0);
	}
}
