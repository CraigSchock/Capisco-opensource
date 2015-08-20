package capisco.commands;

import java.text.MessageFormat;
import java.util.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class Synonyms extends CapiscoCommand
{
	public Synonyms()
	{
	}

	public String helpText()
	{
		return "{0} IDM : Returns the synonyms for a given wikipedia article.";
	}

	public String shortDescription()
	{
		return "M->S+ Get synonyms for given article.";
	}

	protected void executeImpl()
	{
		DBCollection coll = getCollection("labelMapping");
		BasicDBObject query = new BasicDBObject("meaning", Integer.parseInt(data));

		List < String > aList = (List < String >) coll.distinct("label", query);
		printListString(aList);
	}
}
