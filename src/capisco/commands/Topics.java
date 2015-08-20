package capisco.commands;

import java.text.MessageFormat;
import java.util.*;
import java.io.*;

import core.Command;
import core.CommandParser;

import capisco.*;
import capisco.document.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class Topics extends CapiscoCommand
{
	public Topics()
	{
	}

	public String helpText()
	{
		return "{0} <end token> ... text <end token>: identified and disambiguates topics in text";
	}

	public String shortDescription()
	{
		return "Identifies and disambiguates topics in arbitrary text.";
	}

	private ArrayList<String> getSymbols(Document aDocument, DBCollection coll, int number)
	{
		ArrayList<String> results = new ArrayList<String>();

		try
		{
			DocumentCursor cursor = new DocumentCursor(aDocument);
			while (true)
			{
				for (int i = 10; i > 0; i--)
				{
					String phrase = cursor.getTokens(i).toLowerCase();
					if (RelationshipHelper.isLabel(phrase, coll))
					{
						results.add(phrase);
						break;
					}
				}
				if (results.size() >= number)
					break; 
				cursor.consume();
			}
		}
		catch(Exception x)
		{
			// can ignore this one.
		}
		return results;
	}

/*
	private ArrayList<String> getContexts(ArrayList<String> symbols)
	{
		ArrayList<String> contexts = new ArrayList<String>();
		int count = 0;

		for (int i = 0; i < symbols.size() - 1; i++)
		{
			for (int j = i + 1; j < symbols.size(); j++)
			{
				//				System.out.print("rel2: " + symbols.get(i) + "|" + symbols.get(j) + "\t");
				ArrayList<String> results = RelationshipHelper.mutuality(getCollection("labelMapping"),
				symbols.get(i),
				symbols.get(j));
				//				System.out.println("" + results);

				if (results.size() > 0)
				{
					count ++;
					for (int k = 0; k < results.size(); k++)
					{
						if (!contexts.contains(results.get(k)))
							contexts.add(results.get(k));
					}
				}

				if (count >= 3 || contexts.size() > 10)
				{ 
					return contexts; 
				}
			}
		}
		return contexts;
	}
*/

	private void getSymbolTitles(ArrayList<Symbol> symbols)
	{
		for (Symbol symbol : symbols)
		{
			symbol.setTitle(getArticleName(symbol.getMeaning()));
		}
	}

	protected void executeImpl()
	{
		String[] fields = data.split("\\|");
		String text = RelationshipHelper.getText((BufferedReader) getPortHandler().getReader(), fields[0]);
		DBCollection coll = getCollection("labelMapping");
		ArrayList < String > ids = null;

		try
		{
			// Extract contexts from text.
			StringSentenceParser aParser = new StringSentenceParser(text);
			System.out.println("Getting contexts");
			ArrayList<String> symbols = getSymbols(aParser.getDocument(), coll, 100);
			ids = RelationshipHelper.getContexts(coll, symbols);
			System.out.println("Contexts:" + ids);
		}
		catch(Exception x)
		{
			System.out.println("Exception:" + x);
			x.printStackTrace();
		}

		try
		{
			// Disambiguate with contexts
			System.out.println("Disambiguating");
			StringSentenceParser aParser = new StringSentenceParser(text);

			HashMap<String,LabelMapping> mappings = RelationshipHelper.getContextSymbols(coll, ids);
			ArrayList<Symbol> symbols = aParser.findLongestTokens(coll, mappings);
			System.out.println("Total symbols:" + symbols.size());

			ArrayList<Symbol> reducedSymbols = RelationshipHelper.reduceSymbols(symbols);
			System.out.println("Reduced symbols:" + reducedSymbols.size());

			getSymbolTitles(reducedSymbols);
			print("" + reducedSymbols.size());

			if (reducedSymbols.size() <= 0)
				print ("\n"); 

			for (Symbol symbol : reducedSymbols)
			{
				print("|" + symbol + "\n");
			}
			System.out.println("Ending");

		}
		catch(Exception x)
		{
			System.out.println("Exception:" + x);
			x.printStackTrace();
		}

	}

}
