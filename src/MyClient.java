import java.net.*;
import java.io.*;

public class MyClient
{
	public static void main(String [] args)
	{
		try{


			Socket s = new Socket( "localhost", 16789);
			InputStream in = s.getInputStream();
			OutputStream out = s.getOutputStream();
			
			PrintWriter pout = new PrintWriter(out);
			pout.println(args[0]);		// Writes some String to server
			pout.flush(); 				// forces the data through to server
			
			BufferedReader bin = new BufferedReader(new InputStreamReader(in));
			System.out.println(args[0]+" <=returned-as=> " + bin.readLine());

			out.close();
			pout.close();
			s.close();
		}
		catch(IOException ioe)
		{
			System.out.println("IO error");
			ioe.printStackTrace();
		}
	   
	}
}