package lia.searching;

import junit.framework.TestCase;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.util.Vector;

// From chapter 3
public class ScoreTest extends TestCase {
	private Directory directory;
	
	public void setUp() throws Exception {
		directory = new RAMDirectory();
	}
	
	public void tearDown() throws Exception {
		directory.close();
	}
	
	public void indexSingleFieldDocs(Field[] fields) throws Exception {
		IndexWriter writer = new IndexWriter(directory,
				new WhitespaceAnalyzer(), IndexWriter.MaxFieldLength.UNLIMITED);
		for(Field f: fields){
			Document doc = new Document();
			doc.add(f);
			writer.addDocument(doc);
		}
		writer.optimize();
		writer.close();
	}
	
	public static class SimpleSimilarity extends Similarity {

		@Override
		public float coord(int overlap, int maxOverlap) {
			// TODO Auto-generated method stub
			return 1.0f;
		}

		@Override
		public float idf(int docFreq, int numDocs) {
			// TODO Auto-generated method stub
			return 1.0f;
		}
		
		public float idf(Vector terms, Searcher searcher){
			return 1.0f;
		}

		@Override
		public float lengthNorm(String field, int numTerms) {
			// TODO Auto-generated method stub
			return 1.0f;
		}

		@Override
		public float queryNorm(float sumOfSquaredWeights) {
			// TODO Auto-generated method stub
			return 1.0f;
		}

		@Override
		public float sloppyFreq(int distance) {
			// TODO Auto-generated method stub
			return 2.0f;
		}

		@Override
		public float tf(float freq) {
			// TODO Auto-generated method stub
			return freq;
		}
		
	}
	
	public void testSample() throws Exception {
		indexSingleFieldDocs(new Field[]{new Field(
				"contents","x",Field.Store.YES,Field.Index.ANALYZED)});
		IndexSearcher searcher = new IndexSearcher(directory);
		searcher.setSimilarity(new SimpleSimilarity());
		
		Query query = new TermQuery(new Term("contents","x"));
		Explanation explanation = searcher.explain(query, 0); //Describes the score computation for document and query
		System.out.println(explanation);
		
		TopDocs matches = searcher.search(query, 10);
		assertEquals(1,matches.totalHits);
		
		assertEquals(1F,matches.scoreDocs[0].score,0.0);
		
		searcher.close();
	}
	
	public void testWildCard() throws Exception {
		indexSingleFieldDocs(new Field[]{
			new	Field("contents","wild",Field.Store.YES, Field.Index.ANALYZED),
			new Field("contents","child",Field.Store.YES, Field.Index.ANALYZED),
			new Field("contents","mild", Field.Store.YES, Field.Index.ANALYZED),
			new Field("contents","mildew",Field.Store.YES,Field.Index.ANALYZED)
		});
		
		IndexSearcher searcher = new IndexSearcher(directory);
		Query query = new WildcardQuery(new Term("contents","?ild*"));//?*\
		TopDocs matches = searcher.search(query, 10);
		assertEquals("child no match",3,matches.totalHits);
		
		assertEquals("score the same",matches.scoreDocs[0].score,
									  matches.scoreDocs[1].score,0.0);
		assertEquals("score the same",matches.scoreDocs[1].score,
									  matches.scoreDocs[2].score,0.0);
		searcher.close();
	}
	
	public void testFuzzy() throws Exception {
		indexSingleFieldDocs(new Field[]{
			new Field("contents","fuzzy",Field.Store.YES, Field.Index.ANALYZED),
			new Field("contents","wuzzy",Field.Store.YES, Field.Index.ANALYZED)
		});
		
		IndexSearcher searcher = new IndexSearcher(directory);
		Query query = new FuzzyQuery(new Term("contents","wuzza"));
		TopDocs matches = searcher.search(query, 10);
		assertEquals("both close enough",2,matches.totalHits);
		
		assertTrue("wuzzy closer than fuzzy",
				matches.scoreDocs[0].score!=matches.scoreDocs[1].score);
		
		Document doc = searcher.doc(matches.scoreDocs[0].doc);
		assertEquals("wuzza bear","wuzzy",doc.get("contents"));
		searcher.close();
		
	}
}
