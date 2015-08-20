package capisco.commands;

import java.text.MessageFormat;
import java.util.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class Labels extends CapiscoCommand
{
	public Labels()
	{
	}

	public String helpText()
	{
		return "{0} sentence : Returns all possible labels in the sentence.";
	}

	public String shortDescription()
	{
		return "Identify all possible labels in a sentence.";
	}

	private ArrayList < String > fixWords(String[] words)
	{
		StringBuffer sb = new StringBuffer();
		ArrayList < String > resultWords = new ArrayList < String > ();
		for (int i = 0; i < words.length; i++)
		{
			sb.setLength(0);
			for (int j = 0; j < words[i].length(); j++)
			{
				if (Character.isAlphabetic(words[i].charAt(j)) ||
				words[i].charAt(j) == '-' ||
				words[i].charAt(j) == '\'')
				{ sb.append(Character.toLowerCase(words[i].charAt(j))); }
			}
			resultWords.add(sb.toString());
		}
		return resultWords;
	}

	private boolean isLabel(String word, DBCollection coll)
	{
		BasicDBObject query = new BasicDBObject("label", word);
		return coll.count(query) > 0;
	}

	private void processSymbols(ArrayList < String > words, DBCollection coll, ArrayList < String > results)
	{
		StringBuffer sb = new StringBuffer();
		String current = null;

		int j = 0;
		int count = 0;

		while (j < words.size())
		{
			sb.setLength(0);
			for (int i = 0; i + j < words.size(); i++)
			{
				sb.append(" " + words.get(i + j));
				if (isLabel(sb.toString().trim(), coll))
				{
					results.add(sb.toString().trim());
				}
			}
			j++;
		}
	}

/*
Version that only adds the longest symbol.

	private void processSymbols(ArrayList<String> words, DBCollection coll, ArrayList<String> results)
	{
		StringBuffer sb = new StringBuffer();
		String current = null;

		int j = 0;
		int count = 0;

		while (j < words.size())
		{
			current = null;
			count = 0;
			sb.setLength(0);
			for (int i = 0; i+j< words.size(); i++)
			{
				sb.append(" " + words.get(i+j));
				if (isLabel(sb.toString().trim(), coll))
				{
					current = sb.toString().trim();
					count = i;
				}
			}

			if (current != null)
			{
				results.add(current);
				j+=count+1;
			}
			else
				j++;
		}
	}
*/

	protected void executeImpl()
	{
		DBCollection coll = getCollection("labelMapping");
		ArrayList < String > words = fixWords(data.split(" "));

		ArrayList < String > results = new ArrayList < String > ();

		processSymbols(words, coll, results);

		for (String word : results)
		{ print ("|" + word); }
		print ("\n");



/*		BasicDBObject query = new BasicDBObject("meaning", Integer.parseInt(data));

		List<String> aList = (List<String>)coll.distinct("label", query);
		printListString(aList);*/
	}
}
