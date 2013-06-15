package me.joaogl.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThreadNewTry extends Thread {

	private Socket socket = null;
	private static boolean running = false;
	private static PrintWriter socketToUser;
	private BufferedReader socketFromUser;
	private BufferedReader serverCommands;

	public ServerThreadNewTry(Socket socket) {
		super("ServerThread");
		this.socket = socket;
		running = true;
	}

	public void run() {
		try {
			socketToUser = new PrintWriter(socket.getOutputStream(), true);
			socketFromUser = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			serverCommands = new BufferedReader(new InputStreamReader(System.in));

			IncommingManager manager = new IncommingManager();
			socketToUser.println("Connected, Welcome.");
			System.out.println("New Client connected: " + socket.getInetAddress());
			
			while (running) {
				System.out.println("a");
				manager.processInput(socketFromUser.readLine());
				System.out.println("a");
				if (serverCommands.readLine() != null) socketToUser.println(serverCommands.readLine());
				System.out.println("a");
			}

			closeSockets();
		} catch (IOException e) {
			Exception(e);
		}
	}

	public static void close() {
		System.out.println("Closing");
		running = false;
	}

	private void closeSockets() {
		try {
			socketToUser.close();
			socketFromUser.close();
			socket.close();
		} catch (IOException e) {
			Exception(e);
		}
	}

	private void Exception(IOException e) {
		if (e.toString().contains("Connection reset")) System.out.println("Connection Reset: " + socket.getInetAddress() + " - Client disconnected");
		else e.printStackTrace();
	}

	public static void sendToClient(String toClient) {
		socketToUser.println(toClient);
		System.out.println(toClient);
	}
}