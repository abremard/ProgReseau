package stream;

import java.io.*;
import java.net.*;

import stream.SendThread;
import stream.ReceiveThread;

public class MulticastClient  {

    public static void main(String args[]) {
        try {
            // Group IP address
            InetAddress groupAddr = InetAddress.getByName("228.5.6.8");
            int groupPort = 6789;
            // Create a multicast socket
            MulticastSocket s = new MulticastSocket(groupPort);
            // Join the group
            s.joinGroup(groupAddr);

            SendThread st = new SendThread(s, groupAddr, groupPort);
            st.start();

            // Build a datagram packet for a message
            // to send to the group

            // String msg = "Hello";
            // DatagramPacket hi = new
            // DatagramPacket(msg.getBytes(),msg.length(), groupAddr, groupPort);

            // Send a multicast message to the group
            // s.send(hi);

            // Build a datagram packet for response
            // byte[] buf = new byte[1000];
            // DatagramPacket recv = new DatagramPacket(buf, buf.length);
            // Receive a datagram packet response
            // s.receive(recv);

            ReceiveThread rt = new ReceiveThread(s);
            rt.start();

            // OK, I'm done talking - leave the group
            // s.leaveGroup(groupAddr);
            
            while (true) {
                if (!st.running) {
                    rt.running = false;
                }
            }
        } catch (Exception e) {
            System.err.println("Error in MulticastClient:" + e);
            e.printStackTrace();
        }
    }
}