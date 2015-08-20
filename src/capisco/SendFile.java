package capisco;

import java.io.*;
import java.net.*;
import java.util.*;

public class SendFile
{
	public SendFile(String host, String port, String filename) throws IOException
	{
		Socket aSocket = new Socket(host, Integer.parseInt(port));
		BufferedReader sockRead = new BufferedReader(new InputStreamReader(aSocket.getInputStream()));
		BufferedWriter sockWrite = new BufferedWriter(new OutputStreamWriter(aSocket.getOutputStream()));
		
		BufferedReader data = new BufferedReader(new FileReader(filename));
		String line = data.readLine();
		sockWrite.write("topics xxx\n");
		while(line != null)
		{
			sockWrite.write(line);
			line = data.readLine();
		}
		sockWrite.write("\nxxx\n");
		sockWrite.flush();

		String inLine = sockRead.readLine();
		while(!inLine.equals("--message end--"))
		{
			System.out.println(inLine);
			inLine = sockRead.readLine();
		}
	}

	public static void main(String[] args)
	{
		try
		{
			new SendFile(args[0], args[1], args[2]);
		}
		catch(Exception x)
		{
			System.out.println("Exception:" + x);
			x.printStackTrace();
		}
	}

}