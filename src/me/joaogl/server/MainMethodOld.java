package me.joaogl.server;

import java.io.IOException;
import java.net.ServerSocket;

import me.joaogl.server.data.DataManager;

public class MainMethodOld {

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;
		boolean listening = true;
		System.out.println("Starting Server on port 24467.");

		try {
			serverSocket = new ServerSocket(24467);
			System.out.println(" - Server started on port 24467.");
			// Setup planes
			DataManager.setupRegList();
		} catch (IOException e) {
			System.out.println("Error: Could not listen on port 24467. \nPlease check the port. \nServer Closing.");
			System.exit(-1);
		}

		while (listening)
			new ServerThread(serverSocket.accept()).start();

		serverSocket.close();
	}

}
