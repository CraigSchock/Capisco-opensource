package capisco.commands;

import java.text.MessageFormat;
import java.util.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class ArticleID extends CapiscoCommand
{
	public ArticleID()
	{
	}

	public String helpText()
	{
		return "{0} Name : returns the article ID of the wikipedia page specfied by name.";
	}

	public String shortDescription()
	{
		return "Get wikipedia article ID name of the article.";
	}

	protected void executeImpl()
	{
		DBCollection coll = getCollection("article");
		BasicDBObject query = new BasicDBObject("title", data);

		DBCursor cursor = coll.find(query);
		try
		{
			while (cursor.hasNext())
			{
				Map aMap = cursor.next().toMap();
				print("" + aMap.get("_id"));
			}
		}
		catch(Exception x)
		{
			print("Exception:" + x);
		}
		print("\n");

	}
}
