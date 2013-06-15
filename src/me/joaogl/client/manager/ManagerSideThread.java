package me.joaogl.client.manager;

import java.net.*;
import java.io.*;


public class ManagerSideThread extends Thread {
	private Socket socket = null;
	private ManagerSide client = null;
	private DataInputStream streamIn = null;

	public ManagerSideThread(ManagerSide _client, Socket _socket) {
		client = _client;
		socket = _socket;
		open();
		start();
	}

	public void open() {
		try {
			streamIn = new DataInputStream(socket.getInputStream());
		} catch (IOException ioe) {
			System.out.println("Error getting input stream: " + ioe);
			client.stop();
		}
	}

	public void close() {
		try {
			if (streamIn != null) streamIn.close();
		} catch (IOException ioe) {
			System.out.println("Error closing input stream: " + ioe);
		}
	}

	public void run() {
		while (true) {
			try {
				client.handle(streamIn.readUTF());
			} catch (IOException ioe) {
				System.out.println("Server went down.");
				client.stop();
			}
		}
	}
}