package capisco.document;

import capisco.commands.*;
import java.util.*;

public class Symbol implements Comparable
{
	private String text;
	private LabelMapping mapping;

	public Symbol(String text, LabelMapping mapping)
	{
		this.text = text;
		this.mapping = mapping;
	}

	public Integer getMeaning()
	{
		return mapping.getMeaning();
	}

	public void setTitle(String title)
	{
		mapping.setTitle(title);
	}

	public void incrementCount()
	{
		mapping.incrementCount();
	}

	public String getText()
	{
		return text;
	}

	public String toString()
	{
		return mapping.toString();
	}

	public int compareTo(Object other)
	{
		if (other instanceof Symbol)
			return mapping.compareTo(((Symbol) other).mapping);
		return 0;
	}
}
