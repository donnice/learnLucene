package lia.advsearching;

import junit.framework.TestCase;
import lia.common.TestUtil;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class SecurityFilterTest extends TestCase{
	
	private IndexSearcher searcher;
	
	protected void setUp() throws Exception {
		Directory directory = new RAMDirectory();
		IndexWriter writer = new IndexWriter(directory,
									new WhitespaceAnalyzer(),
							IndexWriter.MaxFieldLength.UNLIMITED);
		
		Document document = new Document();
		document.add(new Field("owner",
							   "elwood",
							   Field.Store.YES,
							   Field.Index.NOT_ANALYZED));
		document.add(new Field("keywords",
							   "elwood",
							   Field.Store.YES,
							   Field.Index.ANALYZED));
		writer.addDocument(document);
		
		document = new Document();
		document.add(new Field("owner",
							   "jake",
							   Field.Store.YES,
							   Field.Index.NOT_ANALYZED));
		document.add(new Field("keywords",
							   "jake's sensitive info",
							   Field.Store.YES,
							   Field.Index.ANALYZED));
	}
}
