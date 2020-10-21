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
  	* receives all output from the server and prints it to the console
    * @param clientSocket the client socket  
    * @exception SocketException  Client Ended
    * @exception Exception Unexpected error in ClientLocalThread
  	**/
	public void run() {
    	  try { 
    		while (running) {
                if(socIn != null){
                    String line = socIn.readLine();
                    if(line != null){
                        System.out.println( line );
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

  
