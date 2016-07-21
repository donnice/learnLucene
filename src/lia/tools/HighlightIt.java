package lia.tools;

// page 335

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.util.Version;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

public class HighlightIt {
	private static final String text =
		"In this section we'll show you how to make the simplest " +
	    "programmatic query, searching for a single term, and then " +
	    "we'll see how to use QueryParser to accept textual queries. " +
	    "In the sections that follow, we’ll take this simple example " +
	    "further by detailing all the query types built into Lucene. " +
	    "We begin with the simplest search of all: searching for all " +
	    "documents that contain a single term.";
	
	public static void main(String[] args) throws Exception {
		
		String filename = "test";
		
		String searchText = "term";
		QueryParser parser = new QueryParser(Version.LUCENE_30,
											 "f",
											 new StandardAnalyzer(Version.LUCENE_30));
		Query query = parser.parse(searchText);
		
		SimpleHTMLFormatter formatter = 
			new SimpleHTMLFormatter("<span class=\"highlight\">",
									"</span>");
		
		TokenStream tokens = new StandardAnalyzer(Version.LUCENE_30)
			.tokenStream("f", new StringReader(text));
		
		QueryScorer scorer = new QueryScorer(query,"f");
		
		Highlighter highlighter = new Highlighter(formatter,scorer);
		highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));
		
		String result = 
			highlighter.getBestFragments(tokens, text, 3, "...");	// Highlight best 3 fragments, seperator
		
		FileWriter writer = new FileWriter(filename);
		writer.write("<html>");
		writer.write("<style>\n" + 
			".highlight{\n" + 
			" backgound:yellow;\n" +
			"}\n"+
			"</style>");
		writer.write("<body>");
		writer.write(result);
		writer.write("</body></html>");
		writer.close();
	}
}
