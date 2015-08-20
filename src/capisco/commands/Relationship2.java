package capisco.commands;

import java.text.MessageFormat;
import java.util.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class Relationship2 extends CapiscoCommand
{
	public Relationship2()
	{
	}

	public String helpText()
	{
		return "{0} Symbol1|Symbol2 : Attempts to identify a context-meaning relationship between the two symbols";
	}

	public String shortDescription()
	{
		return "Returns all context-meaning relationships between the defined symbols.";
	}

	protected void executeImpl()
	{
		String[] fields = data.split("\\|");
		ArrayList < String > results = RelationshipHelper.mutuality(getCollection("labelMapping"),
																	fields[0],
																	fields[1]);
		print("" + results.size());
		for (String result : results)
		{ 
			print("|" + result); 
		}
		print("\n");
	}

/*		DBCollection coll = getCollection("labelMapping");
		String[] fields = data.split("\\|");
		BasicDBObject query1 = new BasicDBObject("label", fields[0]);
		BasicDBObject query2 = new BasicDBObject("label", fields[1]);
		List<Integer> list1 = coll.distinct("meaning", query1); // Senses for symbol 1
		List<Integer> list11 = new ArrayList<Integer>(list1);
		List<Integer> list2 = coll.distinct("meaning", query2); // Senses for symbol 2
		List<Integer> list21 = new ArrayList<Integer>(list2);

		BasicDBObject inClause1= new BasicDBObject("$in", list1);
		BasicDBObject context = new BasicDBObject("context", inClause1);
		BasicDBObject inClause2= new BasicDBObject("$in", list2);
		BasicDBObject meaning = new BasicDBObject("meaning", inClause2);
		ArrayList<BasicDBObject> clauseList = new ArrayList<BasicDBObject>();
		clauseList.add(context);
		clauseList.add(meaning);

		ArrayList<String> mappings = new ArrayList<String>();
		ArrayList<String> results = new ArrayList<String>();

//	db.labelMapping.find({$and: [{context: {$in: [13981, 25778403]}}, {meaning: {$in: [13981, 25778403]}}]})

		BasicDBObject query = new BasicDBObject("$and", clauseList);

//		System.out.println("" + query);

		DBCursor cursor = coll.find(query);
		try
		{
			while(cursor.hasNext())
			{
//				System.out.println(""+ cursor.next());
				Map aMap = cursor.next().toMap();
//				print("|" + aMap.get("context")+"," + aMap.get("meaning") +"," +
//					  aMap.get("label"));
				mappings.add("" + aMap.get("context") + "|" + aMap.get("meaning"));
			}
//			print("\n");
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
		inClause1= new BasicDBObject("$in", list21);
		context = new BasicDBObject("context", inClause1);
		inClause2= new BasicDBObject("$in", list11);
		meaning = new BasicDBObject("meaning", inClause2);
		clauseList.add(context);
		clauseList.add(meaning);

//		System.out.println("" + query);
		query = new BasicDBObject("$and", clauseList);
		cursor = coll.find(query);

		try
		{
			while(cursor.hasNext())
			{
//				System.out.println(""+ cursor.next());
				Map aMap = cursor.next().toMap();
				String value = "" + aMap.get("meaning") + "|" + aMap.get("context");
				if (mappings.contains(value) && !results.contains(value))
					results.add(value);
//				print("|" + aMap.get("context")+"," + aMap.get("meaning") +"," +
//					  aMap.get("label"));
			}
//			print("\n");
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
		for (String result: results)
		{
			print(result+"\n");
		}

	}*/
}
