package lia.analysis;

import junit.framework.TestCase;
import lia.analysis.AnalyzerUtils;

public class StopAnalyzerAlternativesTest extends TestCase{
	public void testStopAnalyzer() throws Exception{
		AnalyzerUtils.assertAnalyzesTo(new StopAnalyzer2(),
				"The quick brown...",
				new String[] {"quick","brown"});
	}
	
	public void testStopAnalyzerFlawed() throws Exception {
		AnalyzerUtils.assertAnalyzesTo(new StopAnalyzerFlawed(), 
				"The quick brown...", 
				new String[]{"the","quick","brown"});
	}
	
	public static void main(String[] args) throws Exception {
		AnalyzerUtils.displayTokens(
				new StopAnalyzerFlawed(), "The quick brown...");
	}
}
