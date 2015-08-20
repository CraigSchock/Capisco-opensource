package capisco.document;

import java.util.*;
import java.io.*;
import capisco.document.parsers.*;
import capisco.commands.*;
import com.mongodb.DBCollection;

public class StringSentenceParser
{
	private AbstractParser parser;

	private Document document = new Document();

    public StringSentenceParser(String text) throws IOException
    {
		PushbackReader reader = new PushbackReader(new StringReader(text),2);
		parser.parse(document, reader);
		System.out.println("Uniq Word Count:" + document.getIndex().size());
    }

	public ArrayList<Symbol> getSymbols()
	{
		return document.getProximalSymbols();
	}

	public Document getDocument()
	{
		return document;
	}

	public ArrayList<Symbol> findLongestTokens(DBCollection coll, HashMap<String,LabelMapping> phrases) throws IOException
	{
		String phrase = null; 

//		ArrayList<Token> tokens = document.getTokens();
//		ArrayList<Integer> senseIDs = new ArrayList<Integer>();

		DocumentCursor cursor = new DocumentCursor(document);
		try
		{
			while(true)
			{
				for (int i = 10; i> 0; i--)
				{
					phrase = cursor.getTokens(i).toLowerCase();
					if (phrases.keySet().contains(phrase))
					{
						LabelMapping mapping = phrases.get(phrase);
//						System.out.println("Found phrase!:" + phrases.get(phrase));
						RelationshipHelper.addSymbolsForContext(coll, mapping.getMeaning(), phrases, 
																mapping.getDistance() + 1);
						document.addSymbol(new Symbol(phrase, phrases.get(phrase)));
						break;
					}
				}
				cursor.consume();
			}
		}
		catch(Exception x)
		{
		}

		return document.getProximalSymbols();
	}

    public static void main(String[] args)
    {
		try
		{
//			StringSentenceParser aParser = new StringSentenceParser(args[0]);
//			ArrayList<Symbol> symbols = aParser.findLongestTokens();
//			aParser.printSymbols();
		}
		catch(Exception x)
		{
			System.out.println("Exception:" + x);
			x.printStackTrace();
		}
    }
}
