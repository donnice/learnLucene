package lia.analysis;

import org.apache.nutch.analysis.NutchDocumentAnalyzer;
import org.apache.nutch.searcher.Query;
import org.apache.nutch.searcher.QueryFilters;
import org.apache.hadoop.conf.Configuration;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Token;

import java.io.IOException;
import java.io.StringReader;

public class NutchExample {
	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();
		conf.addResource("nutch-default.xml");
		NutchDocumentAnalyzer analyzer = new NutchDocumentAnalyzer(conf);
	}
}
