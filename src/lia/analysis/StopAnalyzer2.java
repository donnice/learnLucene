package lia.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LetterTokenizer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;

import java.io.Reader;
import java.util.Set;

public class StopAnalyzer2 extends Analyzer{
	
	private Set stopWords;
	
	public StopAnalyzer2(){
		stopWords = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
	}
	
	public StopAnalyzer2(String[] stopWords){
		this.stopWords = StopFilter.makeStopSet(stopWords);
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		// TODO Auto-generated method stub
		return new StopFilter(true,new LowerCaseFilter(new LetterTokenizer(reader)),
				stopWords);
	}

}
