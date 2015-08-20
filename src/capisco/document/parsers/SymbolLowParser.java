package capisco.document.parsers;

import java.io.*;
import capisco.document.*;

public class SymbolLowParser extends AbstractParser
{
	public SymbolLowParser(String category)
	{
		super(category);
	}

	public boolean canAccept(int character)
	{
		if (character >= 33 && character <=64)
			return true;
		else
			return false;
	}

	public void readNextSymbol(Document aDocument, PushbackReader reader) throws IOException 
	{
		int character = reader.read();
		aDocument.addToken(Character.toString((char)character), category);
//		System.out.println("L: " + Character.toString((char)character));
	}
}
