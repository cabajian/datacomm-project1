import java.net.*;
import java.io.*;

/**
 * MyClient class. Generates a single instance of a client, connects to the localhost
 * server, and sends the command line arguments to the server.
 */
public class MyClient {

    /* Server port constant. */
    final static int SERVER_PORT = 16789;

    /*
     * Main function to connect to the server and send/receive communications.
     */
    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                // Must have an argument to send to the server.
                System.out.println("No arguments entered for by the client\n");
            } else {
                // Create the socket and IO streams.
                Socket s = new Socket("localhost", SERVER_PORT);
                InputStream in = s.getInputStream();
                OutputStream out = s.getOutputStream();
                PrintWriter pout = new PrintWriter(out);
                // Send the first argument to the server.
                pout.println(args[0]); // Writes some String to server
                pout.flush(); // forces the data through to server
                // Read the response from the server.
                BufferedReader bin = new BufferedReader(new InputStreamReader(in));
                System.out.println(args[0] + " <=returned-as=> " + bin.readLine());
                // Close the connections.
                out.close();
                pout.close();
                s.close();
            }
        } catch (IOException ioe) {
            System.out.println("IO error");
            ioe.printStackTrace();
        }
    }

} // end MyClient
