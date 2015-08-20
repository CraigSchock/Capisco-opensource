package capisco.document;

// TODO: these are really tokens.  class name should be refactored

public class Token
{
	private Document document;
	private String text;
	private int count = 1;

	private String category;

	public Token(Document document, String text, String category)
	{
		this.document = document;
		this.category = category;
		setText(text);
	}

	private void setText(String aText)
	{
		text = aText;
	}

	public String getText()
	{
		return text;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public String getCategory()
	{
		return category;
	}

	public boolean is(String category)
	{
		return this.category.equals(category);
	}

	public int hashCode()
	{
		return text.hashCode();
	}

	public boolean equals(Object anObject)
	{
		try
		{
			Token aToken = (Token) anObject;
			return text.equals(aToken.text);
		}
		catch(Exception x)
		{
		}
		return false;
	}

	public String toString()
	{
		return "|"+text + (count > 1 ? "[" + count + "]" : ""  + "\t" + category);
	}
}
