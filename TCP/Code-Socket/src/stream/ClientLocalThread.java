/***
 * ClientLocalThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
// import java.util.ArrayList;
import java.util.List;
import stream.EchoServerMultiThreaded;
public class ClientLocalThread
	extends Thread {
	
	private BufferedReader socIn;
	boolean running = true;
	ClientLocalThread(BufferedReader s) {
		this.socIn = s;
	}

 	/**
  	* receives a request from client then sends an echo to the client
  	* @param clientSocket the client socket  
  	**/
	public void run() {
    	  try { 
    		while (running) {
                if(socIn != null){
                    String line = socIn.readLine();
                    if(line != null){
                        System.out.println("echo: " + line );
                    }
                    else{
                        System.err.println("line is null");
                        break;
                    }
                }
                else{
                    System.err.println("socIn ended");
                    break;
                }
            }
    	} catch (SocketException e) {
            System.out.println("CLient Ended");
            running = false;
        }
        catch (Exception e) {
            System.err.println("Error in ClientLocalThread:" + e);
            e.printStackTrace();
        }
       }
  
  }

  
