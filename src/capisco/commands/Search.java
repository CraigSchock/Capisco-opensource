package capisco.commands;

import java.text.MessageFormat;
import java.util.*;
import java.io.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.index.Term;

public class Search extends CapiscoCommand
{
	private final int HITS_PER_PAGE = 10000000;

	public Search()
	{
	}

	public String helpText()
	{
		return "{0} Concept name : Searches for the concept name in the lucene index .";
	}

	public String shortDescription()
	{
		return "Search for concepts in lucene index.";
	}

	protected void executeImpl()
	{
		IndexSearcher lucene = getLuceneIndex();

		BooleanQuery query = new BooleanQuery();
		String musttopics[] = data.split("\\|");
		for (String topic : musttopics)
		{
			Term term = new Term("contents", topic);
			query.add(new TermQuery(term), BooleanClause.Occur.MUST);
		}

		try
		{
			TopDocs results = lucene.search(query, HITS_PER_PAGE);
			ScoreDoc[] hits = results.scoreDocs;
			print(results.totalHits + "|");
			if ("false".equals(get("/lucene/docID")))
			{
				for (int i = 0; i < results.totalHits; i++)
				{
					Document doc = lucene.doc(hits[i].doc);
					print(termMatcher(query, lucene, hits[i].doc, doc.get("path")) + "\n");
				}
			}
			else
			{
				for (int i = 0; i < results.totalHits; i++)
				{
					Document doc = lucene.doc(hits[i].doc);
					print(hits[i].doc + "|");
				}
			}

			print("\n");
		}
		catch(Exception x)
		{
			print("Exception:" + x);
		}

	}

	private String termMatcher(BooleanQuery query, IndexSearcher searcher, int docId, String line) throws IOException
	{
		BooleanClause[] clauses = query.getClauses();
		for (BooleanClause bc : clauses)
		{
			line = line + "|" + searcher.explain(bc.getQuery(), docId).isMatch();
		}
		return line;
	}
}
