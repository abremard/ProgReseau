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
import java.util.Scanner;
// import java.util.ArrayList;
import stream.EchoServerMultiThreaded;

public class ClientThread extends Thread {

	private Socket clientSocket;
	BufferedReader socIn = null;
	PrintStream socOut = null;

	ClientThread(Socket s) {
		this.clientSocket = s;
	}

	/**
	 * Function that writes new messages to the chat history file. Creates
	 * the file if it doesn't exist.
	 * @param str
	 * @exception exception Unexpected error writing into the Chat History file
	 */	
	public synchronized void writeToFile(String str) {
		File chatHistory = new File("history.txt");
		try {
			if (chatHistory.createNewFile()) {
				System.out.println("New history file created");
			}
			FileWriter myWriter = new FileWriter("history.txt", true);
			BufferedWriter bw = new BufferedWriter(myWriter);
			bw.write(str + "\r\n");
			bw.close();
		} catch (Exception e) {
			System.err.println("Error writing into the Chat History file");
		}
	}
	
	/**
	 * Function that reads all the messages in the chat history file.
	 * @exception FileNotFoundException If the chart history files doesn't
	 * exist (no messages saved yet), ignore and keep running.
	 */
	public synchronized void readChat() {
		try {
			File chat = new File("history.txt");
			Scanner myReader = new Scanner(chat);
			socOut.println("Chat History :");
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				socOut.println(data);
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			// ignore and continue
		}
	}

	/**
	 * Client thread that receives the output of the client socket
	 * and sends it to all the other registered sockets on the server.
	 * It deletes the client socket from the server's socket list whren deconnected. 
	 * @param clientSocket the client socket
	 * @exception FileNotFoundException Chat history file not found, keep running.
	 * @exception Exception Unexpected error in ClientThread
	 **/
	public void run() {
		try {
			socOut = new PrintStream(clientSocket.getOutputStream());
			readChat();
			// ephemeral chat history version
			/*
			 * if(EchoServerMultiThreaded.chatHistory.size()>0){
			 * socOut.println("Chat History :"); for (int i = 0; i <
			 * EchoServerMultiThreaded.chatHistory.size(); i++) {
			 * socOut.println(EchoServerMultiThreaded.chatHistory.get(i)); } } else{
			 * socOut.println("Chat History is empty"); }
			 */
			socIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			while (true) {
				String line = socIn.readLine();
				//Removes the socket from the server's socket list on deconnection
				if (line != null && line.equals(".")) {
					for (int i = 0; i < EchoServerMultiThreaded.socketRegistry.size(); i++) {
						if (EchoServerMultiThreaded.socketRegistry.get(i) == clientSocket) {
							EchoServerMultiThreaded.socketRegistry.remove(i);
						}
					}
				} else {
					if (line != null) {
						//Adds typed line to the chat history file
						//EchoServerMultiThreaded.chatHistory.add(line);
						writeToFile(line);
						for (int i = 0; i < EchoServerMultiThreaded.socketRegistry.size(); i++) {
							PrintStream socOut = new PrintStream(
									EchoServerMultiThreaded.socketRegistry.get(i).getOutputStream());
							socOut.println(line);
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			// do nothing
		} catch (Exception e) {
			System.err.println("Error in ClientThread:" + e);
			e.printStackTrace();
		}
	}
}
