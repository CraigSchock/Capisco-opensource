package capisco.commands;

import java.text.MessageFormat;
import java.util.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class KeyMappings extends CapiscoCommand
{
	public KeyMappings()
	{
	}

	public String helpText()
	{
		return "{0} IDC : Returns the symbol/IDM mappings for a given context.";
	}

	public String shortDescription()
	{
		return "C->(S,M)+ Provides the symbol/meaning mappings for a given context.";
	}

	protected void executeImpl()
	{
		DBCollection coll = getCollection("labelMapping");
		BasicDBObject query = new BasicDBObject("context", Integer.parseInt(data));
		HashMap < Integer, String > results = new HashMap < Integer, String > ();

		DBCursor cursor = coll.find(query);
		try
		{
			while (cursor.hasNext())
			{
				Map aMap = cursor.next().toMap();
				Integer meaning = (Integer) aMap.get("meaning");
				String symbol = (String) aMap.get("label");
				String temp = results.put(meaning, symbol);
				if (temp != null)
				{ System.out.println("Duplicate: " + meaning + ":" + symbol + ":" + temp); }
			}
			printNameValueString(results, true);

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
