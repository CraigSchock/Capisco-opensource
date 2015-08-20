package capisco.document.parsers;

import java.io.*;
import capisco.document.*;

public class WordParser extends AbstractParser
{
	private StringBuffer sb = new StringBuffer();

	public WordParser(String category)
	{
		super(category);
	}

	public boolean canAccept(int character)
	{
		if (Character.isAlphabetic(character))
			return true;
		else
			return false;
	}

	public void readNextSymbol(Document aDocument, PushbackReader reader) throws IOException 
	{
		sb.setLength(0);
		int character = reader.read();
		while(Character.isAlphabetic(character) || character == 45 || character == 39)
		{
			if (character == 45)
			{
				int nextChar = reader.read();
				if (nextChar == 45)
				{
					aDocument.addToken(sb.toString(), category);
					reader.unread(nextChar);
					reader.unread(character);
					return;
				}
				reader.unread(nextChar);
			}
			sb.append(Character.toString((char)character));
			character = reader.read();
		}
//		System.out.println("\t\t\t" + sb);
		aDocument.addToken(sb.toString(), category);
		reader.unread(character);
	}
}
