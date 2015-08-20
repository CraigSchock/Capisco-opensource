package capisco.commands;

import java.text.MessageFormat;
import java.util.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class SynMappings extends CapiscoCommand
{
	public SynMappings()
	{
	}

	public String helpText()
	{
		return "{0} Symbol : Returns the context(IDC)/symbol mappings for a given meaning.";
	}

	public String shortDescription()
	{
		return "M->(C,S)+ Provides the context/symbol mappings for a given symbol.";
	}

	protected void executeImpl()
	{
		DBCollection coll = getCollection("labelMapping");
		BasicDBObject query = new BasicDBObject("meaning", Integer.parseInt(data));
		HashMap < Integer, String > results = new HashMap < Integer, String > ();

		DBCursor cursor = coll.find(query);
		try
		{
			while (cursor.hasNext())
			{
				Map aMap = cursor.next().toMap();
				Integer context = (Integer) aMap.get("context");
				String symbol = (String) aMap.get("label");
				String temp = results.put(context, symbol);
				if (temp != null)
				{ System.out.println("Duplicate: " + context + ":" + symbol + ":" + temp); }
			}
			printNameValueString(results, false);

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
