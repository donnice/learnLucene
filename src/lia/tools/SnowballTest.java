package lia.tools;

import junit.framework.TestCase;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.Version;
import lia.analysis.AnalyzerUtils;

//  it serves as a driver of an entire family of stemmers for different languages. 

public class SnowballTest extends TestCase{
	public void testEnglish() throws Exception {
		Analyzer analyzer = new SnowballAnalyzer(Version.LUCENE_30,"English");
		AnalyzerUtils.assertAnalyzesTo(analyzer, "stemming algorithms", 
				new String[] {"stem","algorithm"});
		// get root of word
	}
	
	public void testSpanish() throws Exception {
		Analyzer analyzer = new SnowballAnalyzer(Version.LUCENE_30,"Spanish");
		AnalyzerUtils.assertAnalyzesTo(analyzer, "algoritmos",
				new String[]{"algoritm"});
	}
}
