import java.net.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Multi-threaded Server class. Will initialize a localhost server on
 * the constant port. Contains individual thread-server connection handlers.
 */
public class MyMultiThreadedServer {

    /* ArrayList of string arrays to represent the student names and average grades. */
    private ArrayList<String[]> studentGradeArray = new ArrayList<>();
    /* The server port constant. */
    final static int SERVER_PORT = 16789;

    /*
     *  Main function that calls the MyMultiThreadedServer constructor.
     */
    public static void main(String[] args) {
        new MyMultiThreadedServer();
    }

    /*
     * MyMultiThreadedServer constructor. Initializes a multi-threaded server and
     * continuously polls for socket connections, accepts them, and creates a new thread.
     */
    public MyMultiThreadedServer() {
        ServerSocket ss = null;
        try {
            // Create a new server socket on the port.
            ss = new ServerSocket(SERVER_PORT);
            Socket cs = null;
            System.out.println("Server started at " + SERVER_PORT);
            // Continuously accept new clients trying to connect.
            while (true) {
                cs = ss.accept();
                ThreadServer ths = new ThreadServer(cs);
                ths.start();
            }
        } catch (BindException be) {
            System.out.println("Server already running on this computer, stopping.");
        } catch (IOException ioe) {
            System.out.println("IO Error");
            ioe.printStackTrace();
        }
    }

    /*
     * Adds a student name and average grade to the list as a String[] entry.
     * This function is thread-safe and allows for concurrent execution calls.
     */
    private synchronized void addStudentGrade(String name, double grade) {
        // Create the string array entry.
        String[] entry = new String[2];
        entry[0] = name;
        entry[1] = String.valueOf(grade);
        // Add the entry to the array.
        studentGradeArray.add(entry);
        // Display the entry to the server console.
        System.out.println("Entry: " + name + ": " + grade + " added to Student Grade List");
    }

    /**
     * Inner class of MyMultiThreadedServer to represent a single thread on the server.
     */
    class ThreadServer extends Thread { // member inner class

        /* The socket object. */
        Socket cs;

        /* ThreadServer constructor. Stores the specified socket. */
        public ThreadServer(Socket cs) {
            this.cs = cs;
        }

        /* Run function that automatically executes when the thread is running.
         * Opens I/O streams with the Client and reads an entry from the Client,
         * parses it, stores it in the server, and sends the entry back to the Client.
         */
        public void run() {
            // Variable declarations.
            BufferedReader br;
            PrintWriter opw;
            String clientMsg;
            try {
                // Open the IO streams.
                br = new BufferedReader(new InputStreamReader(cs.getInputStream()));
                opw = new PrintWriter(new OutputStreamWriter(cs.getOutputStream()));
                // Read the message from the client and store it as a single string.
                clientMsg = br.readLine(); // from client
                // Split the string (comma delimited)
                String[] splitMsg = clientMsg.split(",");
                // Expecting a student name and three integer grades. If these conditions
                // fail, send an error response.
                if (splitMsg.length != 4) {
                    clientMsg = "Invalid client format length";
                // Try processing the message.
                } else {
                    try {
                        // Fetch the name and compute the average grade.
                        String name = splitMsg[0];
                        int a = Integer.parseInt(splitMsg[1]);
                        int b = Integer.parseInt(splitMsg[2]);
                        int c = Integer.parseInt(splitMsg[3]);
                        double avg = (a+b+c)/3.0;
                        // Add the name+grade to the server.
                        addStudentGrade(name, avg);
                        // Send the message back.
                        clientMsg = name + "," + String.valueOf(avg);
                    } catch (NumberFormatException n) {
                        clientMsg = "Invalid format found in the client arguments";
                    }
                }
                // Send the (string) message back to the client and flush the output stream.
                opw.println(clientMsg); // to client
                opw.flush();
            } catch (IOException e) {
                System.out.println("Inside catch");
                e.printStackTrace();
            }
        }

    } // end class ThreadServer

} // end MyMultiThreadedServer
