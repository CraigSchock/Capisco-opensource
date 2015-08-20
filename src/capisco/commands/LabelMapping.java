package capisco.commands;

import java.util.*;

public class LabelMapping implements Comparable
{
	String id;
	Integer context;
	Integer meaning;
	String symbol;
	String meaningTitle = "";

	Integer distance;
	int count = 1;

	public LabelMapping(String id, Integer context, Integer meaning, String symbol, Integer distance)
	{
		this.id = id;
		this.context=context;
		this.meaning=meaning;
		this.symbol=symbol;
		this.distance = distance;
	}

	public Integer getDistance()
	{
		return distance;
	}

	public void incrementCount()
	{
		count++;
	}

	public Integer getContext()
	{
		return context;
	}

	public Integer getMeaning()
	{
		return meaning;
	}

	public void setTitle(String meaningTitle)
	{
		this.meaningTitle = meaningTitle;
	}

	public String getTitle()
	{
		return meaningTitle;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(meaningTitle);
		sb.append("|");
		sb.append(symbol);
		sb.append("|");
		sb.append(distance);
		sb.append("|");
		sb.append(count);
		sb.append("|");
		sb.append(context);
		sb.append("|");
		sb.append(meaning);
		sb.append("|");
		sb.append(id);
		return sb.toString();
	}
	public int compareTo(Object other)
	{
		if (other instanceof LabelMapping)
		{
			LabelMapping mapping = (LabelMapping) other;

			if (mapping.distance == this.distance)
			{
				if (mapping.count == this.count)
					return 0;
				if (mapping.count > this.count)
					return 1;
				else
					return -1;
			}
			if (mapping.distance > this.distance)
				return -1;
			return 1;
		}
		return 0;
	}

}
