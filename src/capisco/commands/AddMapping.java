package capisco.commands;

import java.text.MessageFormat;
import java.util.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class AddMapping extends CapiscoCommand
{
	public AddMapping()
	{
	}

	public String helpText()
	{
		return "{0} IDC|IDM|symbol : Adds the specified mapping to the knowledge base.";
	}

	public String shortDescription()
	{
		return "Add a mapping to the knowledge base.";
	}

	protected void executeImpl()
	{
		DBCollection coll = getCollection("labelMapping");
		BasicDBObject addRecord = new BasicDBObject();

		String[] fields = data.split("\\|");

		addRecord.put("context", Integer.parseInt(fields[0]));
		addRecord.put("meaning", Integer.parseInt(fields[1]));
		addRecord.put("label", fields[2].toLowerCase());

		try
		{
			coll.insert(addRecord);
		}
		catch(Exception x)
		{
			print("Exception:" + x);
		}

		print("Success\n");

	}
}
