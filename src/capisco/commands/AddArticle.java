package capisco.commands;

import java.text.MessageFormat;
import java.util.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;

public class AddArticle extends CapiscoCommand
{
	private static Object lock = new Object();
	private static int maxArtID = -1;

	private static int getNextID()
	{
		synchronized(lock)
		{
			return ++maxArtID;
		}
	}

	public AddArticle()
	{
	}

	private int initMaxID()
	{
		System.out.println("*************init max id");
		synchronized(lock)
		{
			if (maxArtID == -1)
			{
				System.out.println("init max inside if");
				DBCollection coll = getCollection("article");
				BasicDBObject query = new BasicDBObject();
				BasicDBObject projection = new BasicDBObject("_id", 1);
				BasicDBObject orderby = new BasicDBObject("_id", -1);
				DBObject result = coll.findOne(query, projection, orderby);
				System.out.println("InitMAxID:" + result.get("_id"));
				maxArtID = Integer.parseInt("" + result.get("_id"));
			}
			return 0;
		}
	}

	public String helpText()
	{
		return "{0} id|title|desc : Adds the specified context to the knowledge base.";
	}

	public String shortDescription()
	{
		return "Add an article to the knowledge base.";
	}

	protected void executeImpl()
	{
		initMaxID();
		DBCollection coll = getCollection("article");
		BasicDBObject addRecord = new BasicDBObject();

		String[] fields = data.split("\\|");

		addRecord.put("_id", Integer.parseInt(fields[0]));
		addRecord.put("title", fields[1]);
		addRecord.put("desc", fields[2].trim());

		try
		{
			coll.insert(addRecord);
		}
		catch(Exception x)
		{
			print("Exception:" + x);
		}

		print("Success\n");

	}
}
