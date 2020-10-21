package stream;

import java.io.*;
import java.net.*;

import stream.SendThread;
import stream.ReceiveThread;

public class MulticastClient  {

    /**
     * The main client. It takes a port number and creates a multicast socket connecting
     * the client to it. Then creates the send and receive threads.
     * @exception exception Unexpected error in MulticastClient
     */
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

            ReceiveThread rt = new ReceiveThread(s);
            rt.start();
            
            while (true) {
                if (st.running == false) {
                    rt.running = false;
                    System.out.println("sdiu");
                    break;
                }
            }
            System.exit(0);
        } catch (Exception e) {
            System.err.println("Error in MulticastClient:" + e);
            e.printStackTrace();
        }
    }
}