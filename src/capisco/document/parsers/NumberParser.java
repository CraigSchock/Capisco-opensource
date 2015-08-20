package capisco.document.parsers;

import java.io.*;
import capisco.document.*;

public class NumberParser extends AbstractParser
{
	private StringBuffer sb = new StringBuffer();

	public NumberParser(String category)
	{
		super(category);
	}

	public boolean canAccept(int character)
	{
		if (Character.isDigit(character))
			return true;
		else
			return false;
	}

	public void readNextSymbol(Document aDocument, PushbackReader reader) throws IOException 
	{
		sb.setLength(0);
		int character = reader.read();
		while(Character.isDigit(character) || character == 46)
		{
			sb.append(Character.toString((char) character));
			character = reader.read();
		}
		reader.unread(character);
		if (sb.charAt(sb.length()-1) == 46)
		{
//			System.out.println("***");
			sb.setLength(sb.length()-1);
			reader.unread(46);
		}
		aDocument.addToken(sb.toString(), category);
//		System.out.println("\t\t" + sb);
	}
}
