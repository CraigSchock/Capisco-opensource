package capisco.commands;

import java.text.MessageFormat;
import java.util.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class KeyPhrases extends CapiscoCommand
{
	public KeyPhrases()
	{
	}

	public String helpText()
	{
		return "{0} IDC : Returns the symbols contained in a wikipedia article.";
	}

	public String shortDescription()
	{
		return "C->S+ Get symbols contained in an article.";
	}

	protected void executeImpl()
	{
		DBCollection coll = getCollection("labelMapping");
		BasicDBObject query = new BasicDBObject("context", Integer.parseInt(data));

		printListString((List < String >) coll.distinct("label", query));
	}
}
