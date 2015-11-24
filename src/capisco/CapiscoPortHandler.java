package capisco;

import java.net.*;
import java.util.*;

import core.CommandQueue;
import core.PortHandler;

import com.mongodb.DBCollection;

import org.apache.lucene.search.IndexSearcher;

public class CapiscoPortHandler extends PortHandler
{
	private Capisco capisco;

	public CapiscoPortHandler(Socket aSocket, CommandQueue aQueue, Capisco mainInstance)
	{
		super(aSocket, aQueue);
		capisco = mainInstance;
	}

	public DBCollection getCollection(String name)
	{
		return capisco.getCollection(get("/env/database"), name);
	}

	public IndexSearcher getLuceneIndex(String path)
	{
		return capisco.getLuceneIndex(path);
	}
}