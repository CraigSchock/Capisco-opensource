package capisco.commands;

import java.text.MessageFormat;
import java.util.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class Senses extends CapiscoCommand
{
	public Senses()
	{
	}

	public String helpText()
	{
		return "{0} Symbol : Returns the meanings of given Symbol.";
	}

	public String shortDescription()
	{
		return "S->M+ Provides the meanings for a given symbol.";
	}

	protected void executeImpl()
	{
		DBCollection coll = getCollection("labelMapping");
		BasicDBObject query = new BasicDBObject("label", data);
		HashMap < Integer, Integer > results = new HashMap < Integer, Integer > ();
		boolean human = isHuman();


		List < Integer > aList = (List < Integer >) coll.distinct("meaning", query);
		if (human)
		{ print (aList.size() + " cases\n"); }
		else
		{ print ("" + aList.size()); }

		for (Integer value : aList)
		{
			if (human)
			{ print(printArticleName(value)); }
			else
			{ print("|" + value); }
		}
		print("\n");

	}
}
