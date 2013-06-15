package me.joaogl.client;

import java.io.*;
import java.net.*;

public class ClientSide {

	private static Socket Socket = null;
	private static PrintWriter out = null;
	private static BufferedReader in = null;
	private static BufferedReader stdIn = null;

	public static void main(String[] args) throws IOException {

		System.out.println("Connecting to the Aircaft Management Server...");

		try {
			System.out.println("Opening sockets.");
			Socket = new Socket("localhost", 24467);
			out = new PrintWriter(Socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
			stdIn = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Sockets open.");
		} catch (UnknownHostException e) {
			forceClose();
		} catch (IOException e) {
			forceClose();
		}

		String fromServer, fromUser;

		while ((fromServer = in.readLine()) != null) {
			if (!fromServer.equalsIgnoreCase("disc")) System.out.println("Server: " + fromServer);
			else {
				System.out.println("Disconnected. Thank you for flying with us, see you soon.");
				break;
			}

			fromUser = stdIn.readLine();
			if (fromUser != null) out.println(fromUser);
		}

		closeSockets();
	}

	private static void forceClose() {
		System.err.println("Server not available.");
		System.exit(1);
	}

	private static void closeSockets() {
		try {
			Socket.close();
			out.close();
			in.close();
			stdIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}