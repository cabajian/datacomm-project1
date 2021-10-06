import java.net.*;
import java.io.*;

public class MyMultiThreadedServer{
	public static void main(String [] args) {
		new MyMultiThreadedServer();
	}
	
	public MyMultiThreadedServer()
	{
		ServerSocket ss = null;

		try {
		  ss = new ServerSocket(16789);
		  Socket cs = null;
		  while(true){ 		
        	  cs = ss.accept(); 				// wait for connection
			  ThreadServer ths = new ThreadServer( cs );
			  ths.start();
		  } // end while
		}
		catch( BindException be ) {
			System.out.println("Server already running on this computer, stopping.");
		}
		catch( IOException ioe ) {
			System.out.println("IO Error");
			ioe.printStackTrace();
		}

	} // end constructor
	
	class ThreadServer extends Thread {  // member inner class
		Socket cs;

		public ThreadServer( Socket cs ) {
			this.cs = cs;
		}
		
		public void run() {
			BufferedReader br;
			PrintWriter opw;
			String clientMsg;
			try {
			  br = new BufferedReader(
						new InputStreamReader( 
							cs.getInputStream()));
			  opw = new PrintWriter(
						new OutputStreamWriter(
							cs.getOutputStream()));
							
			  clientMsg = br.readLine();					// from client        
			  opw.println(clientMsg.toUpperCase());	//to client
			  opw.flush();
			}
			catch( IOException e ) { 
				System.out.println("Inside catch"); 
				e.printStackTrace();
			}
		} // end while
	 } // end class ThreadServer 
} // end MyMultiThreadedServer