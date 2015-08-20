package capisco.commands;

import java.text.MessageFormat;
import java.util.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class Mutuality extends CapiscoCommand
{
	public Mutuality()
	{
	}

	public String helpText()
	{
		return "{0} Symbol1|Symbol2|...|SymbolN : Attempts to identify a mutual dependency between N symbols";
	}

	public String shortDescription()
	{
		return "Returns all mutual context-meaning relationships between the defined symbols that are mutual.";
	}

	protected void executeImpl()
	{
		String[] fields = data.split("\\|");
		ArrayList<String> metadata = new ArrayList<String>();

		for (int i = 0; i< fields.length; i++)
		{
			metadata.add(fields[i]);
		}

		ArrayList<String> results = RelationshipHelper.getContexts(getCollection("labelMapping"), metadata);
		print(results.size() + "|");
		for (String result : results)
			print(result + "|");
		print("\n");

	}

}
