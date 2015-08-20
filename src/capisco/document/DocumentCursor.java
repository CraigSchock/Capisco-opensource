package capisco.document;

import java.util.*;

public class DocumentCursor
{
	private Document document;
	private int index;
	private int index1;

	private ArrayList<Token> temp = new ArrayList<Token>();

	public DocumentCursor(Document document)
	{
		index = 0;
		index1 = 1;
		this.document = document;
	}

	public String getTokens(int count)
	{
		int tempCount = count-1;
		int tempIndex = index;
		temp.clear();

		while(!document.getToken(tempIndex).is("word"))
		{
			tempIndex += 1;
		}

		temp.add(document.getToken(tempIndex));
		while(tempCount > 0)
		{
			tempIndex += 1;
			while(!document.getToken(tempIndex).is("word"))
				tempIndex += 1;
			temp.add(document.getToken(tempIndex));
			tempCount--;
		}
		index1 = tempIndex+1;
		return getText(temp);
	}

	public String getText(ArrayList<Token> tokens)
	{
		StringBuffer sb = new StringBuffer();
		for (Token token : tokens)
			sb.append(token.getText() + " ");
		return sb.toString().trim();
	}

	public void consume()
	{
		index = index1;
	}
}
