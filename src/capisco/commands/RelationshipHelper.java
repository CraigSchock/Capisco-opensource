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

import java.util.*;


public class RelationshipHelper
{
	private static String filename = "./excluded";
	private static HashMap<String,String> excluded = new HashMap<String,String>();	

	static
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String line = in.readLine().trim();
			while (line != null)
			{
				excluded.put(line, line);
				line = in.readLine();
			}
		}
		catch(Exception x)
		{
			System.out.println("Cannot load excluded list:" + x);
		}
		System.out.println("Excluded:" + excluded);	
	}


	public static BasicDBObject getRelationshipQuery(DBCollection coll, String term1, String term2)
	{
		BasicDBObject query1 = new BasicDBObject("label", term1);
		BasicDBObject query2 = new BasicDBObject("label", term2);


		// obtain the possible meanings of each term
		List<Integer> list1 = coll.distinct("meaning", query1);
		List<Integer> list2 = coll.distinct("meaning", query2);

		// And determine if there is a context-meaning relationship between
		// and of the concepts returned from term 1 (context) and
		// the concepts returned from term 2 (meaning).
		BasicDBObject inClause1 = new BasicDBObject("$in", list1);
		BasicDBObject context = new BasicDBObject("context", inClause1);
		BasicDBObject inClause2 = new BasicDBObject("$in", list2);
		BasicDBObject meaning = new BasicDBObject("meaning", inClause2);
		ArrayList<BasicDBObject> clauseList = new ArrayList<BasicDBObject> ();
		clauseList.add(context);
		clauseList.add(meaning);

		BasicDBObject query = new BasicDBObject("$and", clauseList);

		return query;
	}

	public static ArrayList<ArrayList<Integer>> resolveMutuality(DBCollection coll, String term1, String term2)
	{
		ArrayList<Integer> contexts = new ArrayList<Integer>();
		ArrayList<Integer> meanings = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();

		results.add(contexts);
		results.add(meanings);

		DBCursor cursor = coll.find(getRelationshipQuery(coll, term1, term2));
		try
		{
			while (cursor.hasNext())
			{
				Map aMap = cursor.next().toMap();
				contexts.add((Integer)aMap.get("context"));
				meanings.add((Integer)aMap.get("meaning"));
			}
		}
		catch(Exception x)
		{
			System.out.println("Exception" + x);
			return null;
		}
		return results;
	}

	public static ArrayList<String> mutuality(DBCollection coll, String symbol1, String symbol2)
	{
		BasicDBObject query1 = new BasicDBObject("label", symbol1);
		BasicDBObject query2 = new BasicDBObject("label", symbol2);

		List<Integer> list1 = coll.distinct("meaning", query1); 	// Senses for symbol 1
		List<Integer> list11 = new ArrayList<Integer> (list1);
		List<Integer> list2 = coll.distinct("meaning", query2); 	// Senses for symbol 2
		List<Integer> list21 = new ArrayList<Integer> (list2);

		BasicDBObject inClause1 = new BasicDBObject("$in", list1);
		BasicDBObject context = new BasicDBObject("context", inClause1);
		BasicDBObject inClause2 = new BasicDBObject("$in", list2);
		BasicDBObject meaning = new BasicDBObject("meaning", inClause2);

		ArrayList<BasicDBObject> clauseList = new ArrayList<BasicDBObject>();
		clauseList.add(context);
		clauseList.add(meaning);

		ArrayList<String> mappings = new ArrayList<String> ();
		ArrayList<String> results = new ArrayList<String> ();

		BasicDBObject query = new BasicDBObject("$and", clauseList);

		DBCursor cursor = coll.find(query);

		try
		{
			while (cursor.hasNext())
			{
				Map aMap = cursor.next().toMap();
				mappings.add("" + aMap.get("context") + "|" + aMap.get("meaning"));
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

		clauseList.clear();
		inClause1 = new BasicDBObject("$in", list21);
		context = new BasicDBObject("context", inClause1);
		inClause2 = new BasicDBObject("$in", list11);
		meaning = new BasicDBObject("meaning", inClause2);
		clauseList.add(context);
		clauseList.add(meaning);

		query = new BasicDBObject("$and", clauseList);
		cursor = coll.find(query);

		try
		{
			while (cursor.hasNext())
			{
				Map aMap = cursor.next().toMap();
				String value = "" + aMap.get("meaning") + "|" + aMap.get("context");
				if (mappings.contains(value) && !results.contains(value))
				{ 
					results.add(value); 
				}
			}
		}
		catch(Exception x)
		{
			System.out.println("Exception:" + x);
			x.printStackTrace();
		}
		finally
		{
			cursor.close();
		}

		ArrayList<String> resultSet = new ArrayList<String>();

		for (String result : results)
		{
			String[] fields = result.split("\\|");

			if (!resultSet.contains(fields[0]))
			{ 
				resultSet.add(fields[0]); 
			}
			
			if (!resultSet.contains(fields[1]))
			{ 
				resultSet.add(fields[1]); 
			}
		}
		return resultSet;
	}

	public static String getText(BufferedReader aReader, String endToken)
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
				System.out.println("Exception:" + x);
				x.printStackTrace();
				line = endToken;
			}
		}
		return text.toString();
	}

	public static boolean isLabel(String word, DBCollection coll)
	{
		if (excluded.keySet().contains(word))
		{ 
			return false; 
		}

		BasicDBObject query = new BasicDBObject("label", word);
		return coll.count(query) > 0;
	}

	public static void addSymbolsForContext(DBCollection coll, Integer id, HashMap<String,LabelMapping> mappings, Integer distance)
	{
		//		System.out.println("Adding context:" + id);
		BasicDBObject query = new BasicDBObject("context", id);
		DBCursor cursor = coll.find(query);
		try
		{
			while (cursor.hasNext())
			{
				Map aMap = cursor.next().toMap();
				String symbol = (String) aMap.get("label");
				if (!excluded.keySet().contains(symbol) && !mappings.keySet().contains(symbol))
				{
					LabelMapping aMapping = new LabelMapping("" + aMap.get("_id"), id,
															 (Integer)aMap.get("meaning"),
															 (String)aMap.get("label"), distance);
					mappings.put(aMapping.symbol, aMapping);
				}
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

	public static HashMap<String,LabelMapping> getContextSymbols(DBCollection coll, ArrayList<String> ids)
	{
		HashMap<String,LabelMapping> mappings = new HashMap<String,LabelMapping>();

		for (String id : ids)
		{
			addSymbolsForContext(coll, Integer.parseInt(id), mappings, 0);
		}

		return mappings;
	}

	public static ArrayList<Symbol>reduceSymbols(ArrayList<Symbol> symbols)
	{
		HashMap<String,Symbol> results = new HashMap<String,Symbol>();

		for (Symbol symbol : symbols)
		{
			String meaning = symbol.getMeaning().toString();
			if (!results.keySet().contains(meaning))
			{ 
				results.put(meaning, symbol); 
			}
			else
			{ 
				results.get(meaning).incrementCount(); 
			}
		}

		ArrayList<Symbol> sorted = new ArrayList<Symbol>();
		sorted.addAll(results.values());
		Collections.sort(sorted);
		return sorted;
	}

	public static ArrayList<String> getContexts(DBCollection coll, ArrayList<String> symbols)
	{
		ArrayList<String> contexts = new ArrayList<String>();
		int count = 0;

		for (int i = 0; i < symbols.size() - 1; i++)
		{
			for (int j = i + 1; j < symbols.size(); j++)
			{
				//				System.out.print("rel2: " + symbols.get(i) + "|" + symbols.get(j) + "\t");
				ArrayList<String> results = RelationshipHelper.mutuality(coll,
																		 symbols.get(i),
																		 symbols.get(j));
				//				System.out.println("" + results);
				if (results.size() > 0)
				{
					count ++;
					for (int k = 0; k < results.size(); k++)
					{
						if (!contexts.contains(results.get(k)))
						{ 
							contexts.add(results.get(k)); 
						}
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

}
