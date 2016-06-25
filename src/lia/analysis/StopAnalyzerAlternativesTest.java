package lia.analysis;

import junit.framework.TestCase;
import lia.analysis.AnalyzerUtils;

public class StopAnalyzerAlternativesTest extends TestCase{
	public void testStopAnalyzer() throws Exception{
		AnalyzerUtils.assertAnalyzesTo(new StopAnalyzer2(),
				"The quick brown...",
				);
	}

}
