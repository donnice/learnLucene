package lia.searching;

import junit.framework.TestCase;

import lia.common.TestUtil;

import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.store.Directory;

public class NumericRangeQueryTest extends  TestCase{
	public void testInclusive() throws Exception {
		Directory dir = TestUtil.getBookIndexDirectory();
		IndexSearcher searcher = new IndexSearcher(dir);
		
		NumericRangeQuery query = NumericRangeQuery.newIntRange("pubMonth", 
																200605, 
																200609, 
																true, true);
		TopDocs matches = searcher.search(query, 10);
		
		assertEquals(1,matches.totalHits);
		searcher.close();
		dir.close();
	}
}
