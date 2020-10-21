package stream;

import java.io.*;
import java.net.*;

public class SendThread extends Thread {

    BufferedReader stdIn = null;
    MulticastSocket socket = null;
    InetAddress groupAddr = null;
    int groupPort;
    boolean running;
    String username;
    private BufferedReader socIn;
	SendThread(MulticastSocket s, InetAddress grAddr, int grPort) {
        this.socket = s;
        this.groupAddr = grAddr;
        this.groupPort = grPort;
        this.running = true;
	}

 	/**
    * The send thread to send the user's typed messages to the group. 
    * A user first types his username. They can leave by typing " . " 
    * @param s the group's socket
    * @param grAddr the group's address
    * @param grPort the group's port
    * @exception exception represents an unexpected error in the SendThread
  	**/
	public void run() {
    	  try { 
                stdIn = new BufferedReader(new InputStreamReader(System.in));
                while (running) {
                    if(username==null){
                        System.out.println("Enter your username : ");
                        this.username = stdIn.readLine();
                    }
                    String msgTyped =stdIn.readLine();
                    String msgSent = username + " : " + msgTyped;
                    if (msgTyped.equals(".") && msgTyped != null) {
                        this.running = false;
                        socket.leaveGroup(groupAddr);
                        System.exit(0);

                    } else {
                        DatagramPacket packet = new DatagramPacket(msgSent.getBytes(),msgSent.length(), groupAddr, groupPort);
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
