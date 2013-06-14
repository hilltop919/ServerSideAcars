package me.joaogl.server;

import java.io.IOException;
import java.net.ServerSocket;

public class MainMethod {

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;
		boolean listening = true;
		System.out.println("Starting Server on port 24467.");

		try {
			serverSocket = new ServerSocket(24467);
			System.out.println(" - Server started on port 24467.");		
			//Setup planes
			System.out.println("Setting up the data planes.");		
			DataManager.setupRegList();
		} catch (IOException e) {
			System.err.println("Error:");
			System.err.println("Could not listen on port: 24467.");
			System.err.println("Please check the port.");
			System.err.println("Server Closing.");
			System.exit(-1);
		}

		while (listening)
			new ServerThread(serverSocket.accept()).start();

		serverSocket.close();
	}

}
