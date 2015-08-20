package capisco.commands;

import java.text.MessageFormat;
import java.util.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class Mappings extends CapiscoCommand
{
	public Mappings()
	{
	}

	public String helpText()
	{
		return "{0} Symbol : Returns the context/meaning mappings of given Symbol.";
	}

	public String shortDescription()
	{
		return "S->(C,M)+ Provides the context/meaning mappings for a given symbol.";
	}

	protected void executeImpl()
	{
		DBCollection coll = getCollection("labelMapping");
		BasicDBObject query = new BasicDBObject("label", data);
		HashMap < Integer, Integer > results = new HashMap < Integer, Integer > ();
		boolean human = isHuman();

		DBCursor cursor = coll.find(query);
		try
		{
			while (cursor.hasNext())
			{
				Map aMap = cursor.next().toMap();
				Integer context = (Integer) aMap.get("context");
				Integer meaning = (Integer) aMap.get("meaning");
				Integer temp = results.put(context, meaning);

				if (temp != null)
				{ System.out.println("Duplicate: " + context + ":" + meaning + ":" + temp); }
			}
			printNameValue(results);

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
