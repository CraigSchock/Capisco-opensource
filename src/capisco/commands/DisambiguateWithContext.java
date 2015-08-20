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


public class DisambiguateWithContext extends CapiscoCommand
{
	private static String filename = "./excluded";
	private static ArrayList < String > excluded = new ArrayList < String > ();	

	static
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String line = in.readLine();
			while (line != null)
			{
				excluded.add(line.trim());
				line = in.readLine();
			}
		}
		catch(Exception x)
		{
			System.out.println("Cannot load excluded list:" + x);
		}
		System.out.println("Excluded:" + excluded);	
	}

	public DisambiguateWithContext()
	{
	}

	public String helpText()
	{
		return "{0} endToken|contexts(id)|(id...)|<cr> Text ... <cr>endToken : Disambiguates symbols in Text given contexts.";
	}

	public String shortDescription()
	{
		return "contexts,text -> disambiguation of symbols in text given specified contexts.";
	}

	private ArrayList < Integer > getContexts(String[] fields)
	{
		ArrayList < Integer > contexts = new ArrayList < Integer > ();
		for (int i = 1; i < fields.length; i++)
		{
			try
			{
				contexts.add(Integer.parseInt(fields[i]));
			}
			catch(Exception x)
			{
			}
		}
		return contexts;
	}

	public void addSymbolsForContext(Integer id, HashMap < String, LabelMapping > mappings)
	{
		//		System.out.println("Adding context:" + id);
		DBCollection coll = getCollection("labelMapping");
		BasicDBObject query = new BasicDBObject("context", id);
		DBCursor cursor = coll.find(query);
		try
		{
			while (cursor.hasNext())
			{
				Map aMap = cursor.next().toMap();
				LabelMapping aMapping = new LabelMapping("" + aMap.get("_id"),
				id, (Integer)aMap.get("meaning"),
				(String)aMap.get("label"), 0);
				if (!excluded.contains(aMapping.symbol))
				{ mappings.put(aMapping.symbol, aMapping); }
			}
		}
		catch(Exception x)
		{
			System.out.println("Exception" + x);
		}
		finally
		{
			cursor.close();
		}
	}

	private HashMap < String, LabelMapping > getContextSymbols(ArrayList < Integer > ids)
	{
		HashMap < String, LabelMapping > mappings = new HashMap < String, LabelMapping > ();
		DBCollection coll = getCollection("labelMapping");

		for (Integer id : ids)
		{
			addSymbolsForContext(id, mappings);
		}
		return mappings;
	}

	private String getText(BufferedReader aReader, String endToken)
	{
		String line = "";
		StringBuffer text = new StringBuffer();

		while (!endToken.equals(line))
		{
			try
			{
				text.append(line + "\n");
				line = aReader.readLine().trim();
			}
			catch(Exception x)
			{
				line = endToken;
			}
		}
		return text.toString();
	}

	private void getSymbolTitles(ArrayList < Symbol > symbols)
	{
		for (Symbol symbol : symbols)
		{
			symbol.setTitle(getArticleName(symbol.getMeaning()));
		}
	}

	protected void executeImpl()
	{
		DBCollection coll = getCollection("labelMapping");
		String[] fields = data.split("\\|");
		HashMap < String, LabelMapping > mappings = getContextSymbols(getContexts(fields));
		String text = getText((BufferedReader) getPortHandler().getReader(), fields[0]);

		try
		{
			StringSentenceParser aParser = new StringSentenceParser(text);
			ArrayList < Symbol > symbols = aParser.findLongestTokens(coll, mappings);
			getSymbolTitles(symbols);
			print("" + symbols.size());
			for (Symbol symbol : symbols)
			{
				print("|" + symbol + "\n");
			}
		}
		catch(IOException x)
		{
			System.out.println("Exception :" + x);
		}

/*
		BasicDBObject query = new BasicDBObject("context", Integer.parseInt(fields[0]));
		query.append("label", fields[1]);
		HashMap<Integer,Integer> results = new HashMap<Integer,Integer>();
		boolean human = isHuman();

		DBCursor cursor = coll.find(query);
		try
		{
			while(cursor.hasNext())
			{
				Map aMap = cursor.next().toMap();
				Integer meaning = (Integer) aMap.get("meaning");
				results.put(meaning, meaning);
			}
			if (human)
				print(""+results.size() + " case(s)\n");
			else
				print(""+results.size() );

			for(Integer meaning : results.keySet())
			{
				if (human)
					print(printArticleName(meaning));
				else
					print("|" + meaning);
			}
			print ("|\n");
		}
		catch(Exception x)
		{
			System.out.println("Exception" + x);
		}
		finally
		{
				cursor.close();
				}*/

	}
}

