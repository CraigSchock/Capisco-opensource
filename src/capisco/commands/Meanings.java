package capisco.commands;

import java.text.MessageFormat;
import java.util.*;

import core.Command;
import core.CommandParser;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class Meanings extends CapiscoCommand
{
	public Meanings()
	{
	}

	public String helpText()
	{
		return "{0} Context Symbol|Symbol2 : Attempts to identify the meanings of Symbol 2 in all of the possible senses of Context Symbol";
	}

	public String shortDescription()
	{
		return "Returns all possible meanings of a symbol given a context symbol.";
	}

	protected void executeImpl()
	{
		DBCollection coll = getCollection("labelMapping");
		String[] fields = data.split("\\|");
		BasicDBObject query1 = new BasicDBObject("label", fields[0]);
		BasicDBObject query2 = new BasicDBObject("label", fields[1]);
		List < Integer > list1 = coll.distinct("meaning", query1);
		List < Integer > list2 = coll.distinct("meaning", query2);

		BasicDBObject inClause1 = new BasicDBObject("$in", list1);
		BasicDBObject context = new BasicDBObject("context", inClause1);
		BasicDBObject inClause2 = new BasicDBObject("$in", list2);
		BasicDBObject meaning = new BasicDBObject("meaning", inClause2);
		ArrayList < BasicDBObject > clauseList = new ArrayList < BasicDBObject > ();
		clauseList.add(context);
		clauseList.add(meaning);

		//	db.labelMapping.find({$and: [{context: {$in: [13981, 25778403]}}, {meaning: {$in: [13981, 25778403]}}]})

		BasicDBObject query = new BasicDBObject("$and", clauseList);
		HashMap < String, String > results = new HashMap < String, String > ();

		//		System.out.println("" + query);

		DBCursor cursor = coll.find(query);
		try
		{
			while (cursor.hasNext())
			{
				//				System.out.println(""+ cursor.next());
				Map aMap = cursor.next().toMap();
				results.put((String)aMap.get("meaning").toString(), (String)aMap.get("meaning").toString());

				//				print("|" + aMap.get("context")+"," + aMap.get("meaning") +"," +
				//					  aMap.get("label"));
			}
			print("" + results.size());
			for (String value : results.values())
			{
				print("|" + value);
			}

			print("\n");
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
}
