package lia.analysis;

import org.apache.nutch.analysis.NutchDocumentAnalyzer;
import org.apache.nutch.searcher.Query;
import org.apache.nutch.searcher.QueryFilters;
import org.apache.hadoop.conf.Configuration;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

import java.io.IOException;
import java.io.StringReader;

public class NutchExample {
	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();
		conf.addResource("nutch-default.xml");
		// custom analyzer
		NutchDocumentAnalyzer analyzer = new NutchDocumentAnalyzer(conf);
		
		TokenStream ts = analyzer.tokenStream("content",
									new StringReader("The quick brown fox.."));
		OffsetAttribute offsetAttribute = ts.getAttribute(OffsetAttribute.class);
		TermAttribute termAttribute = ts.getAttribute(TermAttribute.class);
		int position = 0;
		while(ts.incrementToken()){
			int startOffset = offsetAttribute.startOffset();
			int endOffset = offsetAttribute.endOffset();
			String term = termAttribute.term();
			int increment = endOffset - startOffset;
			if(increment > 0){
				position += increment;
				System.out.println();
				System.out.print(position+":");
			}
			
			System.out.print("["+
							term+":"+
							startOffset+"->"+
							endOffset+":"+
							termAttribute.getClass().toString());
			System.out.println();
			
			Query nutchQuery = Query.parse("\"the quick brown\"", conf);
			org.apache.lucene.search.Query luceneQuery;
			luceneQuery = new QueryFilters(conf).filter(nutchQuery);
			System.out.println("Translated:"+luceneQuery);
			
		}
	}
}
