package capisco.commands;

import java.text.MessageFormat;
import java.util.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class ArticleDescription extends CapiscoCommand
{
	public ArticleDescription()
	{
	}

	public String helpText()
	{
		return "{0} ID : returns the first paragraph of the wikipedia page specfied by ID.";
	}

	public String shortDescription()
	{
		return "Get first paragraph of the wikipedia article given the wikipedia article ID.";
	}

	protected void executeImpl()
	{
		print(getArticleDescription(Integer.parseInt(data)) + "\n");
	}
}
