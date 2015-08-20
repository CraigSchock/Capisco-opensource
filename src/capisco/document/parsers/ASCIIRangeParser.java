package capisco.document.parsers;

import java.io.*;
import capisco.document.*;

public class ASCIIRangeParser extends AbstractParser
{
	private int low;
	private int high;

	public ASCIIRangeParser(int low, int high, String category)
	{
		super(category);
		this.low = low;
		this.high = high;
	}

	public boolean canAccept(int character)
	{
		if (character >= low && character <=high)
			return true;
		else
			return false;
	}

	public void readNextSymbol(Document aDocument, PushbackReader reader) throws IOException 
	{
		int count = 1;
		int character = reader.read();
		Token token = aDocument.addToken(Character.toString((char)character), category);
		int nextChar = reader.read();

		while (nextChar == character)
		{
			count ++;
			nextChar = reader.read();
		}
		reader.unread(nextChar);
		token.setCount(count);
	}
}
