package lia.searching;

import java.io.IOException;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.document.*;
import org.apache.lucene.search.*;
import org.apache.lucene.util.*;
import javax.servlet.http.*;
import javax.servlet.*;
import lia.extsearch.queryparser.NumericQueryParserTest.NumericDateRangeQueryParser;

public class SearchServletFragment extends HttpServlet{
// Second
	private IndexSearcher searcher;
	
	protected void doGet(HttpServletRequest request,
            HttpServletResponse response) 
        throws ServletException, IOException {
		
		QueryParser parser = new NumericDateRangeQueryParser(Version.LUCENE_30,
															 "contents",
									new StandardAnalyzer(Version.LUCENE_30));
		parser.setLocale(request.getLocale());
		parser.setDateResolution(DateTools.Resolution.DAY);
		
		Query query = null;
		try {
			query = parser.parse(request.getParameter("q"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		TopDocs docs = searcher.search(query, 10);
	}
}
