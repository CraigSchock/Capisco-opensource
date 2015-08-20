package capisco.document;

import java.util.*;

public class Document
{
	private ArrayList<Token> tokens = new ArrayList<Token>();
	private StringBuffer sb = new StringBuffer();
	private HashMap<String,ArrayList<Token>> index = new HashMap<String,ArrayList<Token>>();
	
	private ArrayList<Symbol> proximalSymbols = new ArrayList<Symbol>();

	public Document()
	{
		
	}

	public Token addToken(String text, String category)
	{
		Token token = new Token(this, text, category);
		tokens.add(token);
//		System.out.println("A:" + token);
		if (!index.keySet().contains(text.toLowerCase()))
			index.put(text.toLowerCase(), new ArrayList<Token>());
		index.get(text.toLowerCase()).add(token);

		return token;
	}

	public ArrayList<Token> getTokens()
	{
		return tokens;
	}

	public Token getToken(int index)
	{
		return tokens.get(index);
	}

	public String toString()
	{
		sb.setLength(0);
		
		for(Token token : tokens)
		{
			sb.append(token.toString());
		}
		return sb.toString();
	}

	public HashMap<String,ArrayList<Token>> getIndex()
	{
		return index;
	}

	public Symbol addSymbol(Symbol aSymbol)
	{
//		System.out.println("AddSymbol:" + aSymbol.getText() );
		proximalSymbols.add(aSymbol);
		return aSymbol;
	}

	public ArrayList<Symbol> getProximalSymbols()
	{
		return proximalSymbols;
	}
}
