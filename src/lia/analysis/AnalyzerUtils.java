package lia.analysis;

import junit.framework.Assert;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.StringReader;

public class AnalyzerUtils {
	public static void displayTokensWithFullDetails(Analyzer analyzer,
													String text) throws IOException {
		TokenStream stream = analyzer.tokenStream("contents", new StringReader(text));
		
		TermAttribute term = stream.addAttribute(TermAttribute.class);
		PositionIncrementAttribute posIncr = 
				stream.addAttribute(PositionIncrementAttribute.class);
		OffsetAttribute offset = stream.addAttribute(OffsetAttribute.class);
		TypeAttribute type = stream.addAttribute(TypeAttribute.class);
		
		int position = 0;
		while(stream.incrementToken()){
			
			int increment = posIncr.getPositionIncrement();
			
			if(increment > 0) {
				position += increment;
				System.out.println();
				System.out.print(position+":");
			}
			
			System.out.print("["+
							term.term()+":"+
							offset.startOffset()+"->"+
							offset.endOffset()+":"+
							type.type()+"]");
		}
		System.out.println();
	}

}
