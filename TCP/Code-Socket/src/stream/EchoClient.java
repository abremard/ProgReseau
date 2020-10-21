/***
 * EchoClient
 * Example of a TCP client 
 * Date: 10/01/04
 * Authors:
 */
package stream;

import java.io.*;
import java.net.*;

import stream.ClientLocalThread;

public class EchoClient {

 
  /**
  *  main method
  *  accepts a connection, is the listening thread and creates/manages the send thread (client side)
  * @param args[0] connection host
  * @param args[1] socket port
  * @exception UnknownHostException If host is unknown
  * @exception Exception Unexpected error in EchoClient
  **/
    public static void main(String[] args) throws IOException {

        Socket echoSocket = null;
        PrintStream socOut = null;
        BufferedReader stdIn = null;
        BufferedReader socIn = null;
        String pseudo;

        if (args.length != 2) {
          System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
          System.exit(1);
        }

        try {
      	    // creation socket ==> connexion
      	    echoSocket = new Socket(args[0], Integer.valueOf(args[1]));
	    socIn = new BufferedReader(
	    		          new InputStreamReader(echoSocket.getInputStream()));    
	    socOut= new PrintStream(echoSocket.getOutputStream());
	    stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to:"+ args[0]);
            System.exit(1);
        }
                             
        String sentLine;
        String typedLine;

        //initializes client's username
        System.out.println("Please enter your username");
        pseudo=stdIn.readLine();

        //Creates the Client Receive thread
        ClientLocalThread clientLocalThread = new ClientLocalThread(socIn);
        clientLocalThread.start();

        //Waits for client input then sends it to the server
        while (true) {
          typedLine = stdIn.readLine();
        	sentLine=pseudo + " : " + typedLine;
        	if (typedLine != null && typedLine.equals(".")) {
            clientLocalThread.running = false;
            break;
          }
            socOut.println(sentLine);
        }
      socOut.close();
      socIn.close();
      stdIn.close();
      echoSocket.close();
    }
}


