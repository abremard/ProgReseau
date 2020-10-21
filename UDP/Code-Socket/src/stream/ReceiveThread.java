package stream;

import java.io.*;
import java.net.*;

public class ReceiveThread extends Thread {

    BufferedReader stdIn = null;
    MulticastSocket socket = null;
    boolean running;

    private BufferedReader socIn;
	
	ReceiveThread(MulticastSocket s) {
        this.socket = s;
        this.running = true;
	}

 	/**
  	* The receive thread to listen and receive all the users messages on the group.  
    * @param s the group's socket
    * @exception exception represents an unexpected error in the ReceiveThread 
  	**/
	public void run() {
    	  try { 
                while (running) {
                    byte[] buf = new byte[1000];
                    DatagramPacket recv = new DatagramPacket(buf, buf.length);
                    // Receive a datagram packet response
                    socket.receive(recv);
                    String received = new String(recv.getData(), 0, recv.getLength());
                    System.out.println(received);
                }
            }
        catch (Exception e) {
            System.err.println("Error in ReceiveThread:" + e);
            e.printStackTrace();
        }
       }

}
