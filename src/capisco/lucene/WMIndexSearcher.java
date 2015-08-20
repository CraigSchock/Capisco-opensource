package capisco.lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.util.Version;

/** Simple command-line based search demo. */
public class WMIndexSearcher {

	private WMIndexSearcher() {}

	/** Simple command-line based search demo. */
	public static void main(String[] args) throws Exception {
		String usage =
				"Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir] [-field f] [-repeat n] [-queries file] [-query string] [-raw] [-paging hitsPerPage]\n\nSee http://lucene.apache.org/core/4_1_0/demo/ for details.";
		if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
			System.out.println(usage);
			System.exit(0);
		}

// vars
		String index = "/home/mr97/index/";
		String field = "contents";
		String queries = null;
		int repeat = 0;
		boolean raw = false;
		String queryString = null;
		int hitsPerPage = 1000;

		for(int i = 0;i < args.length;i++) {
			if ("-index".equals(args[i])) {
				index = args[i+1];
				i++;
			} else if ("-field".equals(args[i])) {
				field = args[i+1];
				i++;
			} else if ("-queries".equals(args[i])) {
				queries = args[i+1];
				i++;
			} else if ("-query".equals(args[i])) {
				queryString = args[i+1];
				i++;
			} else if ("-repeat".equals(args[i])) {
				repeat = Integer.parseInt(args[i+1]);
				i++;
			} else if ("-raw".equals(args[i])) {
				raw = true;
			} else if ("-paging".equals(args[i])) {
				hitsPerPage = Integer.parseInt(args[i+1]);
				if (hitsPerPage <= 0) {
					System.err.println("There must be at least 1 hit per page.");
					System.exit(1);
				}
				i++;
			}
		}
// intialization
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
//
		// :Post-Release-Update-Version.LUCENE_XY:
		//Analyzer analyzer = new CustomAnalyzer();

		BufferedReader in = null;
		if (queries != null) {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(queries), StandardCharsets.UTF_8));
		} else {
			in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
		}

		while (true) {
			if (queries == null && queryString == null) {                        // prompt the user
				System.out.println("Enter query: ");
			}

			String line = queryString != null ? queryString : in.readLine();

			if (line == null || line.length() == -1) {
				break;
			}

			line = line.trim();
			if (line.length() == 0) {
				break;
			}
			searching(line, "", "", searcher, 1000);
		}
		// :Post-Release-Update-Version.LUCENE_XY:
		//   QueryParser parser = new QueryParser(field, analyzer);

		reader.close();
	}

	public static String[] searching(String shouldquery, String mustquery, String notquery, IndexSearcher searcher, int hitsPerPage) throws ParseException, IOException
	{
		BooleanQuery query = new BooleanQuery();

		if(!shouldquery.equals("")){
			System.out.println();
			String shouldtopics[] = shouldquery.split("\\|");
			for(String topic : shouldtopics)
			{
				Term term = new Term("contents", topic);
				query.add(new TermQuery(term), BooleanClause.Occur.SHOULD);
			}
		}
		if(!mustquery.equals("")){
			String musttopics[] = mustquery.split("\\|");
			for(String topic : musttopics)
			{
				Term term = new Term("contents", topic);
				query.add(new TermQuery(term), BooleanClause.Occur.MUST);
			}
		}
		if(!notquery.equals("")){
			String nottopics[] = notquery.split("\\|");
			for(String topic : nottopics)
			{
				Term term = new Term("contents", topic);
				query.add(new TermQuery(term), BooleanClause.Occur.MUST_NOT);
			}
		}
		TopDocs results = searcher.search(query, hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;
		String[] resultpaths = new String[results.totalHits];
		for (int i = 0; i < results.totalHits; i++) 
		{
			Document doc = searcher.doc(hits[i].doc);
			resultpaths[i]= termMatcher(query, searcher, hits[i].doc, doc.get("path"));
			
			
			//Explanation explain = searcher.explain(query, hits[i].doc);
			//System.out.println(explain.toHtml());
			System.out.println(resultpaths[i]);
		}
		return resultpaths;
	}
	
	static String termMatcher(BooleanQuery query, IndexSearcher searcher,int docId, String line) throws IOException
	{
		BooleanClause[] clauses = query.getClauses();
		for (BooleanClause bc : clauses)
		{
				line = line +"|"+ searcher.explain(bc.getQuery(), docId).isMatch();
		}
		return line;
	}
}
