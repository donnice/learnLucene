package lia.analysis;

import java.io.Reader;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.LowerCaseTokenizer;
import org.apache.lucene.analysis.Tokenizer;

public final class SimpleAnalyzer extends Analyzer {

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		// TODO Auto-generated method stub
		return new LowerCaseTokenizer(reader);
	}
	
	public TokenStream reusableTokenStream(String fieldName, Reader reader)
		throws IOException {
		Tokenizer tokenizer = (Tokenizer) getPreviousTokenStream();
		if(tokenizer == null){
			tokenizer = new LowerCaseTokenizer(reader);
			setPreviousTokenStream(tokenizer);
		} else
			tokenizer.reset(reader);
		return tokenizer;
	}
	
	public static void main(String[] args) throws IOException {
		AnalyzerUtils.displayTokensWithFullDetails(new SimpleAnalyzer(), "The quick brown fox...");
	}
}
