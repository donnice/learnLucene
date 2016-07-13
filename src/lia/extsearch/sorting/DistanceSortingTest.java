package lia.extsearch.sorting;

import junit.framework.TestCase;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FieldDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

public class DistanceSortingTest extends TestCase{
	private RAMDirectory directory;
	private IndexSearcher searcher;
	private Query query;
	
	protected void setUp() throws Exception {
		directory = new RAMDirectory();
		IndexWriter writer = 
				new IndexWriter(directory,new WhitespaceAnalyzer(),
						IndexWriter.MaxFieldLength.UNLIMITED);
		addPoint(writer, "El Charro","restaurant",1,2);
		addPoint(writer, "Cafe Poca Cosa","restaurant",5,9);
		addPoint(writer, "Los Betos","restaurant",9,6);
		addPoint(writer, "Nico's Taco Shop","restaurant",3,8);
	}
	
	private void addPoint(IndexWriter writer,String name, String type, int x, int y)
					throws IOException{
		Document doc = new Document();
		// Custom sorting implementations are most useful in situations 
		// when the sort criteria can’t be determined during indexing.
		doc.add(new Field("name",name,Field.Store.YES,Field.Index.NOT_ANALYZED));
		doc.add(new Field("type",type,Field.Store.YES,Field.Index.NOT_ANALYZED));
		doc.add(new Field("x",Integer.toString(x),Field.Store.YES,Field.Index.NOT_ANALYZED));
		doc.add(new Field("y",Integer.toString(y),Field.Store.YES,Field.Index.NOT_ANALYZED));
		writer.addDocument(doc);
	}
	
	public void testNearestRestaurantToHome() throws Exception {
		Sort sort = new Sort(new SortField("unused",
				new DistanceComparatorSource(0,0)));
	}
}
