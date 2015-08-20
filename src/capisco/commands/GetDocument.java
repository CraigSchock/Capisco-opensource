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

public class GetDocument extends CapiscoCommand
{

	public GetDocument()
	{
	}

	public String helpText()
	{
		return "{0} docID : Get document text from the repository.";
	}

	public String shortDescription()
	{
		return "Get a document by lucene doc ID.";
	}

	protected void executeImpl()
	{
		IndexSearcher lucene = getLuceneIndex();
		String docBase = get("/lucene/docbase") + "/" +
		get("/lucene/docset") + "/";

		try
		{
			Document doc = lucene.doc(Integer.parseInt(data));
			BufferedReader aReader = new BufferedReader( new FileReader(docBase + doc.get("path")));
			String line = aReader.readLine();

			while (line != null)
			{
				print(line + "\n");
				line = aReader.readLine();
			}
		}
		catch(Exception x)
		{
			print("Exception getting Doc:" + x + "\n");
		}
	}
}
