package lia.indexing;

import java.io.IOException;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.util.Version;

public class Fragments {
	public static void indexNumbersMethod() {
		new Field("size","4096",
				  Field.Store.YES,
				  Field.Index.NOT_ANALYZED);
		new Field("price","10.99",
				  Field.Store.YES,
				  Field.Index.NOT_ANALYZED);
		new Field("author","Arthur C. Clark",
				  Field.Store.YES,
				  Field.Index.NOT_ANALYZED);
	}
	
	public static final String COMPANY_DOMAIN = "example.com";
	public static final String BAD_DOMAIN = "yucky-domain.com";
	
	private String getSenderEmail(){
		return "bob@smith.com";
	}
	
	private String getSenderName(){
		return "Bob Smith";
	}
	
	private String getSenderDomain(){
		return COMPANY_DOMAIN;
	}
	
	private String getSubject() {
	    return "Hi there Lisa";
	}

	private String getBody() {
	    return "I don't have much to say";
	}
	
	private boolean isImportant(String lowerDomain){
		return lowerDomain.endsWith(COMPANY_DOMAIN);
	}
	
	private boolean isUnimportant(String lowerDomain){
		return lowerDomain.endsWith(BAD_DOMAIN);
	}
	
	// RAM is much faster
	public void ramDirExample() throws Exception {
		Analyzer analyzer = new WhitespaceAnalyzer();
		
		// START
		Directory ramDir = new RAMDirectory();
		IndexWriter writer = new IndexWriter(ramDir, analyzer,
									IndexWriter.MaxFieldLength.UNLIMITED);
		// END
	}
	
	public void dirCopy() throws Exception {
		Directory otherDir = null;
		
		Directory ramDir = new RAMDirectory(otherDir);
	}
	
	public void addIndexes() throws Exception {// use the addIndexes(Directory[]) method to write to a single index
		Directory otherDir = null;
		Directory ramDir = null;
		Analyzer analyzer = null;
		
		IndexWriter writer = new IndexWriter(otherDir, analyzer,
									IndexWriter.MaxFieldLength.UNLIMITED);
		writer.addIndexesNoOptimize(new Directory[] {ramDir}); //
	}
	
	public void docBoostMethod() throws IOException {
		
		Directory dir = new RAMDirectory();
	    IndexWriter writer = new IndexWriter(dir, new StandardAnalyzer(Version.LUCENE_30), IndexWriter.MaxFieldLength.UNLIMITED);
	    
	    // START
	    Document doc = new Document();
	    String senderEmail = getSenderEmail();
	    String senderName = getSenderName();
	    String subject = getSubject();
	    String body = getBody();
	    
	    doc.add(new Field("senderEmail",senderEmail,
	    				  Field.Store.YES,
	    				  Field.Index.NOT_ANALYZED));
	    doc.add(new Field("senderName",senderName,
	    				  Field.Store.YES,
	    				  Field.Index.ANALYZED));
	    doc.add(new Field("subject",subject,
	    				  Field.Store.YES,
	    				  Field.Index.ANALYZED));
	    doc.add(new Field("body",body,
	    				  Field.Store.NO,
	    				  Field.Index.ANALYZED));
	    String lowerDomain = getSenderDomain().toLowerCase();
	    if(isImportant(lowerDomain)){
	    	doc.setBoost(1.5F);	// importance
	    } else if (isUnimportant(lowerDomain)){
	    	doc.setBoost(0.1F);
	    }
	    writer.addDocument(doc);
	    // END
	    writer.close();
	}
	
	public void fieldBoostMethod() throws IOException{
		String senderName = getSenderName();
		String subject = getSubject();
		
		// START
		Field subjectField = new Field("subject",subject,
									   Field.Store.YES,
									   Field.Index.ANALYZED);
		subjectField.setBoost(1.2F);
		// END
	}
	
	public void numberField(){
		Document doc = new Document();
		// START
		doc.add(new NumericField("price").setDoubleValue(19.99));
	}
}
