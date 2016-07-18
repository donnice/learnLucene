package lia.advsearching;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.util.OpenBitSet;

import java.io.IOException;

public class SpecialsFilter extends Filter{
	// Here, we fetch the ISBNs of the specials we want to enable for searching
	
	private SpecialsAccessor accessor;
	
	public SpecialsFilter(SpecialsAccessor accessor) {
		this.accessor = accessor;
	}

	@Override
	public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
		// allows direct access to the array of words storing the bits
		OpenBitSet bits = new OpenBitSet(reader.maxDoc());
		
		String[] isbns = accessor.isbns();
		
		int[] docs = new int[1];
		int[] freqs = new int[1];
		
		for(String isbn:isbns){
			if(isbn != null){
				// jump to term
				TermDocs termDocs = reader.termDocs(new Term("isbn",isbn));
				int count = termDocs.read(docs, freqs);
				if(count == 1)
					bits.set(docs[0]);
			}
		}
		return bits;
	}
	
	public String toString() {
		return "SpecialsFilter";
	}
	
}
