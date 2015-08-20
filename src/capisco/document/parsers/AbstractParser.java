package capisco.document.parsers;

import capisco.document.*;

import java.io.*;

public abstract class AbstractParser
{
	private static AbstractParser head = new WordParser("word");

	private AbstractParser next = null;
	protected String category;

	static
	{
		head = new WordParser("word");
		head.addParser(new WhitespaceParser("space"));
		head.addParser(new NumberParser("number"));
		head.addParser(new ASCIIRangeParser(33, 64, "symbol"));
		head.addParser(new ASCIIRangeParser(91, 96, "symbol"));
		head.addParser(new ASCIIRangeParser(123, 126, "symbol"));
	}

	public AbstractParser(String category)
	{
		this.category=category;
	}

	public static void parse(Document aDocument, PushbackReader reader) throws IOException
	{
		int character = reader.read();
		while (character != -1)
		{
			reader.unread(character);
			head.parseSymbol(aDocument, reader, character);
			character = reader.read();
		}
	}

	public void addParser(AbstractParser aParser)
	{
		if (next != null)
			next.addParser(aParser);
		else
			next = aParser;
	}

	public void parseSymbol(Document aDocument, PushbackReader reader, int character) throws IOException
	{
		if (canAccept(character))
			readNextSymbol(aDocument, reader);
		else if (next != null)
			next.parseSymbol(aDocument, reader, character);
		else
		{
			reader.read();
//			System.out.println("No Handler for '" + character + "'");
		}
	}

	public abstract boolean canAccept(int character);
	public abstract void readNextSymbol(Document aDocument, PushbackReader reader) throws IOException;

}
