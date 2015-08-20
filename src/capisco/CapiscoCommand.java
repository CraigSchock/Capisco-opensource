package capisco;

import java.util.*;

import core.Command;

import com.mongodb.DBCollection;

import org.apache.lucene.search.IndexSearcher;

public abstract class CapiscoCommand extends Command
{
	public boolean isHuman()
	{
		return "true".equals(get("human"));
	}

	public String printArticleName(int value)
	{
		CapiscoPortHandler aHandler = (CapiscoPortHandler) getPortHandler();
		return ArticleNameHelper.printArticleName(aHandler,value);
	}

	public String getArticleName(int value)
	{
		CapiscoPortHandler aHandler = (CapiscoPortHandler) getPortHandler();
		return ArticleNameHelper.getArticleName(aHandler,value);
	}

	public String getArticleDescription(int value)
	{
		CapiscoPortHandler aHandler = (CapiscoPortHandler) getPortHandler();
		return ArticleNameHelper.getArticleDescription(aHandler,value);
	}

	public DBCollection getCollection(String name)
	{
		return ((CapiscoPortHandler) getPortHandler()).getCollection(name);
	}

	public IndexSearcher getLuceneIndex()
	{
		CapiscoPortHandler portHandler = (CapiscoPortHandler) getPortHandler();
		String luceneIndexPath = get("/lucene/docbase") + "/" + 
			get("/lucene/docset") + "/index";
		return portHandler.getLuceneIndex(luceneIndexPath);
	}

	public void printList(List<Integer> aList)
	{
		boolean human = isHuman();

		if (human)
			print (aList.size() + " cases\n");
		else
			print ("" + aList.size());

		for (Integer value: aList)
			if (human)
				print(printArticleName(value));
			else
				print("|" + value);
		print("\n");

	}

	public void printListString(List<String> aList)
	{
		boolean human = isHuman();

		if (human)
			print (aList.size() + " cases\n");
		else
			print ("" + aList.size());

		for (String value: aList)
			if (human)
				print("\"" + value + "\"\n");
			else
				print("|" + value);
		print("\n");

	}

	public void printNameValue(HashMap<Integer, Integer>aList)
	{
		boolean human = isHuman();

		if (human)
			print (aList.size() + " cases\n");
		else
			print ("" + aList.size());

		for(Integer key : aList.keySet())
		{
			if (human)
				print(key + "," + aList.get(key) + "\t" + 
					  getArticleName(key) + ":" + 
					  getArticleName(aList.get(key)) + "\n");
			else
				print("|" + key +","+aList.get(key));
		}
		print ("|\n");
	}

	public void printNameValueString(HashMap<Integer, String>aList, boolean reverse)
	{
		boolean human = isHuman();

		if (human)
			print (aList.size() + " cases\n");
		else
			print ("" + aList.size());

		for(Integer key : aList.keySet())
		{
			if (human && reverse)
				print(key + "\t\"" + aList.get(key) + "\"\t" +  getArticleName(key) + "\n");
			else if (human)
				print(key + "\t" + getArticleName(key) + "\t\"" + aList.get(key) +  "\"\n");
			else if (reverse)
				print("|" + aList.get(key) + "," + key);
			else
				print("|" + key + "," + aList.get(key) );
		}
		print ("|\n");
	}

}