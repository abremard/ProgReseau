/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.util.List;
// import java.util.ArrayList;
import stream.EchoServerMultiThreaded;

public class ClientThread
	extends Thread {
	
	private Socket clientSocket;
	
	ClientThread(Socket s) {
		this.clientSocket = s;
	}

 	/**
  	* receives a request from client then sends an echo to the client
  	* @param clientSocket the client socket
  	**/
	public void run() {
    	  try {
    		BufferedReader socIn = null;
    		socIn = new BufferedReader(
				new InputStreamReader(clientSocket.getInputStream()));
    		while (true) {
			  String line = socIn.readLine();
			//   List<PrintStream> streamList = new ArrayList<PrintStream>();

			if (line!=null && line.equals(".")) {
				for (int i = 0; i < EchoServerMultiThreaded.socketRegistry.size(); i++) {
					if (EchoServerMultiThreaded.socketRegistry.get(i) == clientSocket) {
						EchoServerMultiThreaded.socketRegistry.remove(i);
					}
				}
			} else {
				for (int i = 0; i < EchoServerMultiThreaded.socketRegistry.size(); i++) {
					PrintStream socOut = new PrintStream(EchoServerMultiThreaded.socketRegistry.get(i).getOutputStream());
					// streamList.add(socOut);
					if(line != null){
						socOut.println(line);
					}
				}
			}
    		  
    		}
    	} catch (Exception e) {
			System.err.println("Error in ClientThread:" + e);
			e.printStackTrace(); 
        }
       }
  
  }

  
