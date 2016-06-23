package lia.indexing;

import junit.framework.*;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.index.Term;

import java.io.IOException;

public class IndexingTest extends TestCase{
	protected String[] ids = {"1","2"};
	protected String[] unindexed = {"Netherlands","Italy"};
	protected String[] unsorted = {"Amsterdam has lots of bridges",
			"Venice has lots of canals"};
	protected String[] text = {"Amsterdam","Venice"};
}
