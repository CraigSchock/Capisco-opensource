package capisco.commands;

import java.text.MessageFormat;
import java.util.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class ArticleName extends CapiscoCommand
{
	public ArticleName()
	{
	}

	public String helpText()
	{
		return "{0} ID : returns the article name of the wikipedia page specfied by ID.";
	}

	public String shortDescription()
	{
		return "Get wikipedia article given the wikipedia article ID.";
	}

	protected void executeImpl()
	{
		print(getArticleName(Integer.parseInt(data)) + "\n");
	}
}
