package capisco.document.parsers;

import java.io.*;
import capisco.document.*;

public class WhitespaceParser extends AbstractParser
{
	public WhitespaceParser(String category)
	{
		super(category);
	}

	public boolean canAccept(int character)
	{
		if (Character.isWhitespace(character))
			return true;
		else
			return false;
	}

	public void readNextSymbol(Document aDocument, PushbackReader reader) throws IOException 
	{
		int count = 1;
		int character = reader.read();
		Token token;

		if (character == 13)
			return;

		if (character == 10)
			token = aDocument.addToken("\\n", "newline");
		else
			token = aDocument.addToken(Character.toString((char)character), category);
		int nextChar = reader.read();

		if (nextChar == 13)
			nextChar = reader.read();

		while(nextChar == character)
		{
			count ++;
			nextChar = reader.read();
			if (nextChar == 13)
				nextChar = reader.read();

//			System.out.println("nextI:" + nextChar);
		}
		reader.unread(nextChar);
		token.setCount(count);
//		System.out.println("Token:" + token);
	}
}
