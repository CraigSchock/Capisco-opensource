package capisco;

import java.util.*;

import capisco.*;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class ArticleNameHelper
{
	public static String getArticleName(CapiscoPortHandler aHandler, int id)
	{
		DBCollection coll = aHandler.getCollection("article");
		BasicDBObject query = new BasicDBObject("_id", id);

		DBCursor cursor = coll.find(query);
		try
		{
			if (cursor.hasNext())
			{
				Map aMap = cursor.next().toMap();
				return (String)aMap.get("title");
			}
		}
		catch(Exception x)
		{
			System.out.println("Exception:" + x);
		}
		return "";
	}

	public static String getArticleDescription(CapiscoPortHandler aHandler, int id)
	{
		DBCollection coll = aHandler.getCollection("article");
		BasicDBObject query = new BasicDBObject("_id", id);

		DBCursor cursor = coll.find(query);
		try
		{
			if (cursor.hasNext())
			{
				Map aMap = cursor.next().toMap();
				return (String)aMap.get("desc");
			}
		}
		catch(Exception x)
		{
			System.out.println("Exception:" + x);
		}
		return "";
	}

	public static String printArticleName(CapiscoPortHandler aHandler, int id)
	{
		return id + "\t" + getArticleName(aHandler, id) + "\n";
	}
}
