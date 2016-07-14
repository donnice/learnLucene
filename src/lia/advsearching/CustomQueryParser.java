package lia.advsearching;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.util.Version;

// override the custom query parser

public class CustomQueryParser extends QueryParser{
	public CustomQueryParser(Version matchVersion,
			String field, Analyzer analyzer){
		super(matchVersion,field,analyzer);
	}
	
	protected final Query getWildcardQuery(String field, String termStr)
		throws ParseException {
		throw new ParseException("Wild card is not allowed!");
	}
	
	protected Query getFuzzyQuery(String field, String term, float minSimilarity)
		throws ParseException {
			throw new ParseException("Fuzzy queries are not allowed!");
		
	}
	
	protected Query getFieldQuery(String field, String queryText, int slop)
		throws ParseException {
		Query orig = super.getFieldQuery(field, queryText,slop);
		
		// A Query that matches documents containing a particular
		// sequence of terms
		if(!(orig instanceof PhraseQuery))			// only override phraseQuery
			return orig;
		
		PhraseQuery pq = (PhraseQuery)orig;
		Term[] terms = pq.getTerms();
		SpanTermQuery[] clauses = new SpanTermQuery[terms.length];
		for(int i = 0; i < terms.length; i++)
			clauses[i] = new SpanTermQuery(terms[i]);	// pull all terms
		SpanNearQuery query = new SpanNearQuery(clauses,slop,true);	// create SpanNearQuery
		
		return query;
	}
}
