package capisco;

import java.io.*;
import java.net.*;
import java.util.*;

import core.CommandQueue;
import core.Main;
import core.PortHandler;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

public class Capisco extends Main
{
	private MongoClient mongoClient;
	private DB db;
	private HashMap<String, IndexSearcher> luceneIndexes = 
		new HashMap<String, IndexSearcher>();


	public Capisco(int aPort, int threadCount)
	{
		super(aPort, threadCount);
		initMongoDBAccess();

		start();
	}

	private void initMongoDBAccess()
	{
		try
		{
			mongoClient = new MongoClient();
			db = mongoClient.getDB("wikipedia");
		}
		catch (Exception x)
		{
			System.out.println("Exception caught while attempting to open MongoDB: " + x);
			System.exit(-1);
		}
	}

	private IndexSearcher initLuceneAccess(String indexLocation)
	{
		System.out.println("IndexPath:" + indexLocation);
		try
		{
			IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation)));
			return new IndexSearcher(reader);
		}
		catch(Exception x)
		{
			System.out.println("Exception caught while attempting to open Lucene index:" + x);
			x.printStackTrace();
		}
		return null;
	}

	public DBCollection getCollection(String name)
	{
		return db.getCollection(name);
	}

	protected PortHandler createPortHandler(Socket serverSocket,
			CommandQueue commandQueue) throws IOException {
		return new CapiscoPortHandler(serverSocket, commandQueue, this);
	}

	public IndexSearcher getLuceneIndex(String path)
	{
		if (!luceneIndexes.keySet().contains(path))
		{
			luceneIndexes.put(path, initLuceneAccess(path));
		}
		return luceneIndexes.get(path);
	}

	public static void main(String[] args)
	{
		new Capisco(3434, 8);
	}
}