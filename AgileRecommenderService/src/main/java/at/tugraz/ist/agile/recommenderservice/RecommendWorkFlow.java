package at.tugraz.ist.agile.recommenderservice;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import at.tugraz.ist.agile.recommenderservice.models.Workflow;


public class RecommendWorkFlow {
	
	public List<Workflow> recommendedWorkflows;
	RecommendWorkFlow(){
		this.recommendedWorkflows = new ArrayList<Workflow>();
	}
	
	public void getRecommendation(String querystr) throws IOException, ParseException {
		
		System.out.println("LUCENE will calculate recommendation for workflows");
		
		// 0. Specify the analyzer for tokenizing text.
		//    The same analyzer should be used for indexing and searching
		StandardAnalyzer analyzer = new StandardAnalyzer();
		
		// 1. create the index
		Directory index = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);

		IndexWriter w = new IndexWriter(index, config);
		addWF(w);
		w.close();

		// the "title" arg specifies the default field to use
		// when no field is explicitly specified in the query.
		Query q = null;
		try {
			q = new QueryParser("datatag", analyzer).parse(querystr);
		} catch (org.apache.lucene.queryparser.classic.ParseException e) {
			e.printStackTrace();
		}

		// 3. search
		int hitsPerPage = 10;
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		// 4. display results
		System.out.println("Found " + hits.length + " hits.");
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			System.out.println((i + 1) + ". TYPE:" + d.get("type") + "\tDATATAG:" + d.get("datatag") + "\tDATAOWNER:" + d.get("dataowner") +"\tHREF:" + d.get("href"));
			if(!recommendedWorkflows.contains(new Workflow(d.get("type"), d.get("datatag"), d.get("dataowner"), d.get("href"))))
				recommendedWorkflows.add(new Workflow(d.get("type"), d.get("datatag"), d.get("dataowner"), d.get("href")));
			
			boolean alreadyPlaced = false;
			Iterator<Workflow> iterator = recommendedWorkflows.iterator();
		    while(iterator.hasNext()) {
		        Workflow setElement = iterator.next();
		        if(setElement.getHref().equals(d.get("href"))) {
		        	alreadyPlaced = true;
		        	break;
		        }
		    }
		    
			if(!alreadyPlaced)
				recommendedWorkflows.add(new Workflow(d.get("type"), d.get("datatag"), d.get("dataowner"), d.get("href")));
		
		}

		// reader can only be closed when there
		// is no need to access the documents any more.
		reader.close();
	}

	private void addWF(IndexWriter w) throws IOException {
		
		for(int i=0;i<recommendedWorkflows.size();i++){
			Workflow wf = recommendedWorkflows.get(i);
			
			Document doc = new Document();

			doc.add(new TextField("datatag", wf.datatag, Field.Store.YES));
	
			// use a string field for datatag because we don't want it tokenized
			doc.add(new StringField("type", wf.type, Field.Store.YES));
			doc.add(new StringField("dataowner", wf.dataowner, Field.Store.YES));
			doc.add(new StringField("href", wf.href, Field.Store.YES));
			w.addDocument(doc);
		}		
	}
	
}