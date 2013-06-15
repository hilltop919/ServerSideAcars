package me.joaogl.client;

import java.io.*;
import java.net.*;

public class ClientSideNewTry {

	private static boolean running = false;
	private static Socket Socket = null;
	private static PrintWriter out = null;
	private static BufferedReader in = null;
	private static BufferedReader fromUser = null;

	public static void main(String[] args) throws IOException {
		System.out.println("Connecting to the Aircaft Management Server...");

		try {
			Socket = new Socket("192.168.1.65", 24467);
			out = new PrintWriter(Socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
			fromUser = new BufferedReader(new InputStreamReader(System.in));

			running = true;
		} catch (UnknownHostException e) {
			forceClose();
		} catch (IOException e) {
			forceClose();
		}

		while (running) {
			if (in.readLine().equals("disc")) close();
			if (fromUser.readLine() != null) out.println(fromUser.readLine());

			System.out.println("Server: " + in.readLine());
		}

		closeSockets();
	}

	private static void close() {
		System.out.println("Disconnecting...");
		running = false;
	}

	private static void forceClose() {
		System.err.println("Server not available.");
		System.exit(1);
	}

	private static void closeSockets() {
		try {
			System.out.println("Program closing.");
			out.close();
			in.close();
			fromUser.close();
			Socket.close();
			System.out.println("Program closed.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}