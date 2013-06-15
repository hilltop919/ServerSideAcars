package me.joaogl.client;

import java.io.*;
import java.net.*;

public class ClientSide {

	private static Socket Socket = null;
	private static PrintWriter out = null;
	private static BufferedReader in = null;
	private static BufferedReader stdIn = null;
	private static boolean running = false;

	public static void main(String[] args) throws IOException {
		running = true;
		System.out.println("Type connect (pilot id) - to connect");
		while (running) {
			stdIn = new BufferedReader(new InputStreamReader(System.in));
			String fromUser = stdIn.readLine();
			if (fromUser != null && fromUser.contains("connect")) {
				if (fromUser.contains(" ")) {
					String[] input = fromUser.split(" ");
					if (input.length > 1 && input[1] != null) {
						System.out.println("Connected as " + input[1]);
						startCom(input[1]);
					} else System.out.println("Type connect (pilot id) - to connect");
				} else System.out.println("Type connect (pilot id) - to connect");
			} else System.out.println("You are not connected");
		}
	}

	private static void startCom(String input) {
		System.out.println("Connecting to the Aircaft Management Server...");

		try {
			System.out.println("Opening sockets.");
			Socket = new Socket("localhost", 24467);
			out = new PrintWriter(Socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
			stdIn = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Sockets ready.");

			String fromServer, fromUser;
			out.println("newcom " + input);

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
		} catch (UnknownHostException e) {
			forceClose();
		} catch (IOException e) {
			forceClose();
		}
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