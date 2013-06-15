package me.joaogl.client;

import java.io.*;
import java.net.*;

public class ClientSide {

	private static boolean running = false;
	private static Socket Socket = null;
	private static PrintWriter out = null;
	private static BufferedReader in = null;
	private static BufferedReader fromUser = null;

	public static void main(String[] args) throws IOException {

		System.out.println("Connecting to the Aircaft Management Server...");

		System.out.println("10%");

		try {
			System.out.println("15%");
			Socket = new Socket("localhost", 24467);
			out = new PrintWriter(Socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
			System.out.println("50%");
		} catch (UnknownHostException e) {
			System.err.println("Server not available.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Server not available.");
			System.exit(1);
		}

		System.out.println("55%");
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String fromServer;
		String fromUser;
		System.out.println("90%");
		System.out.println("100%  Done");

		while ((fromServer = in.readLine()) != null) {
			System.out.println("Server: " + fromServer);
			if (fromServer.equals("Disconnected. Thank you for flying with us, see you soon.")) break;

			fromUser = stdIn.readLine();
			if (fromUser != null) {
				out.println(fromUser);
			}
		}

		out.close();
		in.close();
		stdIn.close();
		Socket.close();
	}
}