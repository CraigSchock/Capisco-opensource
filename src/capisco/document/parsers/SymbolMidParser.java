package capisco.document.parsers;

import java.io.*;
import capisco.document.*;

public class SymbolMidParser extends AbstractParser
{
	public SymbolMidParser(String category)
	{
		super(category);
	}

	public boolean canAccept(int character)
	{
		if (character >= 91 && character <=96)
			return true;
		else
			return false;
	}

	public void readNextSymbol(Document aDocument, PushbackReader reader) throws IOException 
	{
		int character = reader.read();
		aDocument.addToken(Character.toString((char)character), category);
				
//		System.out.println("M: " + Character.toString((char)character));
	}
}
