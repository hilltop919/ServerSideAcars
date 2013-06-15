package me.joaogl.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
	private Socket socket = null;

	public ServerThread(Socket socket) {
		super("ServerThread");
		this.socket = socket;
	}

	public void run() {
		try {
			IncommingManager manager = new IncommingManager();

			PrintWriter socketToUser = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader socketFromUser = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String outputLine, inputLine;

			socketToUser.println("Connected, welcome.");
			System.out.println("Client " + socket.getInetAddress() + " connected.");

			while ((inputLine = socketFromUser.readLine()) != null) {
				outputLine = manager.processInput(inputLine);
				socketToUser.println(outputLine);

				if (outputLine.equals("disc")) {
					System.out.println("Client " + socket.getInetAddress() + " disconnected.");
					break;
				}
			}
			socketToUser.close();
			socketFromUser.close();
			socket.close();
		} catch (IOException e) {
			Exception(e);
		}
	}

	private void Exception(IOException e) {
		if (e.toString().contains("Connection reset")) System.out.println("Client " + socket.getInetAddress() + " lost connection.");
		else e.printStackTrace();
	}

	private void disconnect() {

	}
}