package capisco.commands;

import java.text.MessageFormat;
import java.util.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class Inlinks extends CapiscoCommand
{
	public Inlinks()
	{
	}

	public String helpText()
	{
		return "{0} IDM : Returns the IDCs that contain a link to the IDM.";
	}

	public String shortDescription()
	{
		return "M->C+ Get IDCs for given IDM.";
	}

	protected void executeImpl()
	{
		DBCollection coll = getCollection("labelMapping");
		BasicDBObject query = new BasicDBObject("meaning", Integer.parseInt(data));
		boolean human = isHuman();

		printList((List < Integer >) coll.distinct("context", query));
	}
}
