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
  *  accepts a connection, receives a message from client then sends an echo to the client
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
        String typedLine;;
        System.out.println("Please enter your username");
        pseudo=stdIn.readLine();
        ClientLocalThread clientLocalThread = new ClientLocalThread(socIn);
        clientLocalThread.start();
        //System.out.println("Chat History : ");
        //for (int i = 0; i < EchoServerMultiThreaded.chatHistory.size(); i++){
          //System.out.println(EchoServerMultiThreaded.chatHistory.get(i));
        //}
        //System.out.println(EchoServerMultiThreaded.chatHistory.size());
        while (true) {
          typedLine = stdIn.readLine();
        	sentLine=pseudo + " : " + typedLine;
        	if (typedLine != null && typedLine.equals(".")) {
            //System.out.println(EchoServerMultiThreaded.chatHistory.size());
            clientLocalThread.running = false;
            break;
          }
            socOut.println(sentLine);
            //EchoServerMultiThreaded.chatHistory.add(sentLine);
        }
      socOut.close();
      socIn.close();
      stdIn.close();
      echoSocket.close();
    }
}


