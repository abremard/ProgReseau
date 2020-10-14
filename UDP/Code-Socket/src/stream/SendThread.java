package stream;

import java.io.*;
import java.net.*;

public class SendThread extends Thread {

    BufferedReader stdIn = null;
    MulticastSocket socket = null;
    InetAddress groupAddr = null;
    int groupPort;
    boolean running;
    
    private BufferedReader socIn;
	SendThread(MulticastSocket s, InetAddress grAddr, int grPort) {
        this.socket = s;
        this.groupAddr = grAddr;
        this.groupPort = grPort;
        this.running = true;
	}

 	/**
  	* receives a request from client then sends an echo to the client
  	* @param clientSocket the client socket  
  	**/
	public void run() {
    	  try { 
                stdIn = new BufferedReader(new InputStreamReader(System.in));
                while (running) {
                    String msg = stdIn.readLine();
                    if (msg.equals(".") && msg != null) {
                        socket.leaveGroup(groupAddr); 
                    } else {
                        DatagramPacket packet = new DatagramPacket(msg.getBytes(),msg.length(), groupAddr, groupPort);
                        socket.send(packet);   
                    }             
                }
            }
        catch (Exception e) {
            System.err.println("Error in SendThread:" + e);
            e.printStackTrace();
        }
       }

}
