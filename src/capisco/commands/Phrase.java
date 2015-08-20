package capisco.commands;

import java.text.MessageFormat;
import java.util.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class Phrase extends CapiscoCommand
{
	public Phrase()
	{
	}

	public String helpText()
	{
		return "{0} IDC|IDM : Returns the symbol for a context/meaning pair.";
	}

	public String shortDescription()
	{
		return "(C,M)->S Provides the symbol that has a specific meaning in a context.";
	}

	protected void executeImpl()
	{
		DBCollection coll = getCollection("labelMapping");
		String[] fields = data.split("\\|");
		BasicDBObject query = new BasicDBObject("context", Integer.parseInt(fields[0]));
		query.append("meaning", Integer.parseInt(fields[1]));
		HashMap < String, String > results = new HashMap < String, String > ();

		DBCursor cursor = coll.find(query);
		try
		{
			while (cursor.hasNext())
			{
				Map aMap = cursor.next().toMap();
				String symbol = (String) aMap.get("label");
				results.put(symbol, symbol);
			}
			print("" + results.size());

			for (String symbol : results.keySet())
			{
				print("|" + symbol);
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
