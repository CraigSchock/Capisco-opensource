package capisco.commands;

import java.text.MessageFormat;
import java.util.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class Contexts extends CapiscoCommand
{
	public Contexts()
	{
	}

	public String helpText()
	{
		return "{0} Symbol : Returns the contexts of given Symbol.";
	}

	public String shortDescription()
	{
		return "S->C+ Provides the contexts for a given symbol.";
	}

	protected void executeImpl()
	{
		DBCollection coll = getCollection("labelMapping");
		BasicDBObject query = new BasicDBObject("label", data);
		HashMap < Integer, Integer > results = new HashMap < Integer, Integer > ();
		boolean human = isHuman();

		printList((List < Integer >) coll.distinct("context", query));

	}
}
