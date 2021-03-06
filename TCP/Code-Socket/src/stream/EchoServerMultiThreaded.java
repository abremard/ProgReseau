/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import stream.*;
import java.util.List;
import java.util.ArrayList;

public class EchoServerMultiThreaded  {
	public static List<Socket> socketRegistry = new ArrayList<Socket>();
	public static List<String> chatHistory = new ArrayList<String>();
 	/**
	* main method that creates the server and manages incoming connection requests.
	* Creates a client thread (server side) at each connection.
	* @param EchoServer server's port
	* @exception exception Unexpected error in EchoServerMultiThreaded
  	**/
       public static void main(String args[]){ 
        ServerSocket listenSocket;
        
  	if (args.length != 1) {
          System.out.println("Usage: java EchoServer <EchoServer port>");
          System.exit(1);
  	}
	try {
		listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
		System.out.println("Server ready..."); 
		while (true) {
			Socket clientSocket = listenSocket.accept();
			System.out.println("Connexion from:" + clientSocket.getInetAddress());
			ClientThread ct = new ClientThread(clientSocket);
			//EchoServerListener st = new EchoServerListener(clientSocket, ct.socIn);
			socketRegistry.add(clientSocket);
			ct.start();
			//st.start();

		}
        } catch (Exception e) {
			System.err.println("Error in EchoServerMultiThreaded:" + e);
			e.printStackTrace();
        }
      }
  }

  
