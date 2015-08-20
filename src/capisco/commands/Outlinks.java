package capisco.commands;

import java.text.MessageFormat;
import java.util.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class Outlinks extends CapiscoCommand
{
	public Outlinks()
	{
	}

	public String helpText()
	{
		return "{0} IDC : Returns the IDMs that are linked from the IDC.";
	}

	public String shortDescription()
	{
		return "C->M+ Get IDMs for given IDC.";
	}

	protected void executeImpl()
	{
		DBCollection coll = getCollection("labelMapping");
		BasicDBObject query = new BasicDBObject("context", Integer.parseInt(data));

		printList((List < Integer >) coll.distinct("meaning", query));

	}
}
