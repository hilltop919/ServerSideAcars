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
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String inputLine, outputLine;
			IncommingManager manager = new IncommingManager();
			outputLine = "Connected, welcome.";
			out.println(outputLine);
			System.out.println("Client " + socket.getInetAddress() + " connected.");
			while ((inputLine = in.readLine()) != null) {
				outputLine = manager.processInput(inputLine);
				out.println(outputLine);
				if (outputLine.equals("disc")) {
					System.out.println("Client " + socket.getInetAddress() + " disconnected.");
					break;
				}
			}
			out.close();
			in.close();
			socket.close();
		} catch (IOException e) {
			Exception(e);
		}
	}

	private void Exception(IOException e) {
		if (e.toString().contains("Connection reset")) System.out.println("Client " + socket.getInetAddress() + " lost connection.");
		else e.printStackTrace();
	}
}