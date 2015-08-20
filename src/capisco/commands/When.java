package capisco.commands;

import java.text.MessageFormat;
import java.util.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class When extends CapiscoCommand
{
	public When()
	{
	}

	public String helpText()
	{
		return "{0} IDM|Symbol : Returns the IDCs for an IDM/symbol pair.";
	}

	public String shortDescription()
	{
		return "(S,M)->C When does the specified symbol have a specific meaning?";
	}

	protected void executeImpl()
	{
		DBCollection coll = getCollection("labelMapping");
		String[] fields = data.split("\\|");
		BasicDBObject query = new BasicDBObject("meaning", Integer.parseInt(fields[0]));
		query.append("label", fields[1]);
		HashMap < Integer, Integer > results = new HashMap < Integer, Integer > ();
		boolean human = isHuman();

		DBCursor cursor = coll.find(query);
		try
		{
			while (cursor.hasNext())
			{
				Map aMap = cursor.next().toMap();
				Integer context = (Integer) aMap.get("context");
				results.put(context, context);
			}

			if (human)
			{ print("" + results.size() + " cases\n"); }
			else
			{ print("" + results.size()); }

			for (Integer context : results.keySet())
			{
				if (human)
				{ print(printArticleName(context)); }
				else
				{ print("|" + context); }
			}
			print ("|\n");
		}
		catch(Exception x)
		{
			System.out.println("Exception" + x);
		}
		finally
		{
			cursor.close();
		}

	}
}
