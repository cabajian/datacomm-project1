import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class MyMultiThreadedServer{
    public static void main(String [] args) {
        new MyMultiThreadedServer();
    }

    ArrayList<String[]> studentGradeArray = new ArrayList<>();

    public MyMultiThreadedServer() {
        ServerSocket ss = null;

        try {
            ss = new ServerSocket(16789);
            Socket cs = null;
            while(true){
                cs = ss.accept(); // wait for connection
                ThreadServer ths = new ThreadServer( cs );
                ths.start();
            } // end while
        } catch( BindException be ) {
            System.out.println("Server already running on this computer, stopping.");
        } catch( IOException ioe ) {
            System.out.println("IO Error");
            ioe.printStackTrace();
        }

    } // end constructor

    public synchronized void addStudentGrade(String name, double grade) {
        String[] entry = new String[2];
        entry[0] = name;
        entry[1] = String.valueOf(grade);
        studentGradeArray.add(entry);
    }

    class ThreadServer extends Thread { // member inner class
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

                clientMsg = br.readLine(); // from client
                String[] splitMsg = clientMsg.split(",");
                if (splitMsg.length != 4) {
                    clientMsg = "Invalid format!";
                } else {
                    String name = splitMsg[0];
                    double avg = (Double.parseDouble(splitMsg[1]) + Double.parseDouble(splitMsg[2]) + Double.parseDouble(splitMsg[3]))/3.0;
                    addStudentGrade(name, avg);
                    clientMsg = name + "," + String.valueOf(avg);
                }
                opw.println(clientMsg);	// to client
                opw.flush();
            } catch( IOException e ) {
                System.out.println("Inside catch");
                e.printStackTrace();
            }
        } // end while
     } // end class ThreadServer
} // end MyMultiThreadedServer