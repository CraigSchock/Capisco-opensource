package capisco.commands;

import java.text.MessageFormat;
import java.util.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class Disambiguate extends CapiscoCommand
{
	public Disambiguate()
	{
	}

	public String helpText()
	{
		return "{0} IDC|Symbol : Returns the meaning for a context/symbol pair.";
	}

	public String shortDescription()
	{
		return "(C,S)->M Provides the meaning of a symbol in a context.";
	}

	protected void executeImpl()
	{
		DBCollection coll = getCollection("labelMapping");
		String[] fields = data.split("\\|");
		BasicDBObject query = new BasicDBObject("context", Integer.parseInt(fields[0]));
		query.append("label", fields[1]);
		HashMap < Integer, Integer > results = new HashMap < Integer, Integer > ();
		boolean human = isHuman();

		DBCursor cursor = coll.find(query);
		try
		{
			while (cursor.hasNext())
			{
				Map aMap = cursor.next().toMap();
				Integer meaning = (Integer) aMap.get("meaning");
				results.put(meaning, meaning);
			}
			if (human)
			{ print("" + results.size() + " case(s)\n"); }
			else
			{ print("" + results.size()); }

			for (Integer meaning : results.keySet())
			{
				if (human)
				{ print(printArticleName(meaning)); }
				else
				{ print("|" + meaning); }
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
