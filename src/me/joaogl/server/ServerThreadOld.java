package me.joaogl.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThreadOld extends Thread {
	private Socket socket = null;
	private PrintWriter socketToUser;
	private BufferedReader socketFromUser;
	private String pilotId;

	public ServerThreadOld(Socket socket) {
		super("ServerThread");
		this.socket = socket;
	}

	public void run() {
		try {
			IncommingManager manager = new IncommingManager();

			socketToUser = new PrintWriter(socket.getOutputStream(), true);
			socketFromUser = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String outputLine, inputLine;

			while ((inputLine = socketFromUser.readLine()) != null) {
				if (inputLine.contains("newcom ")) {
					String[] name = inputLine.split(" ");
					if (name.length > 1 && name[1] != null) {
						pilotId = name[1];
						break;
					}
				}
			}
			socketToUser.println("Connected, welcome " + pilotId + ".");
			System.out.println("Client " + socket.getInetAddress() + " connected.");

			while ((inputLine = socketFromUser.readLine()) != null) {
				outputLine = manager.processInput(inputLine);
				socketToUser.println(outputLine);

				if (outputLine.equals("disc")) {
					System.out.println("Client " + socket.getInetAddress() + " disconnected.");
					break;
				}
			}

			closeSockets();
		} catch (IOException e) {
			Exception(e);
		}
	}

	private void Exception(IOException e) {
		if (e.toString().contains("Connection reset")) System.out.println("Client " + socket.getInetAddress() + " lost connection.");
		else e.printStackTrace();
	}

	private void closeSockets() {
		try {
			socketToUser.close();
			socketFromUser.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}