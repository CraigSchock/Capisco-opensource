package capisco.document.parsers;

import java.io.*;
import capisco.document.*;

public class SymbolHighParser extends AbstractParser
{

	public SymbolHighParser(String category)
	{
		super(category);
	}

	public boolean canAccept(int character)
	{
		if (character >= 123 && character <=126)
			return true;
		else
			return false;
	}

	public void readNextSymbol(Document aDocument, PushbackReader reader) throws IOException 
	{
		int character = reader.read();
		aDocument.addToken(Character.toString((char)character), category);
		System.out.println("H: " + Character.toString((char)character));
	}
}
