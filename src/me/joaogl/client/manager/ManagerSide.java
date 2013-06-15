package me.joaogl.client.manager;

import java.net.*;
import java.io.*;


public class ManagerSide implements Runnable {
	private Socket socket = null;
	private Thread thread = null;
	private DataInputStream console = null;
	private DataOutputStream streamOut = null;
	private ManagerSideThread client = null;
	private static boolean running = false;

	public static void main(String args[]) {
		running = true;

		System.out.println("Type connect username - to connect");
		while (running) {
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			String fromUser = null;
			try {
				fromUser = stdIn.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (fromUser != null && fromUser.contains("connect")) {
				if (fromUser.contains(" ")) {
					String[] input = fromUser.split(" ");
					if (input.length > 1 && input[1] != null) {
						System.out.println("Connecting as administrator " + input[1]);
						running = false;
						ManagerSide client = new ManagerSide(24467, input[1]);
					} else System.out.println("Type connect username - to connect");
				} else System.out.println("Type connect username - to connect");
			} else System.out.println("You are not connected");
		}
	}

	public ManagerSide(int serverPort, String name) {
		System.out.println("Establishing connection. Please wait ...");
		try {
			socket = new Socket("localhost", serverPort);
			System.out.println("Connected to the Aircraft Management Server - Rio Sul Virtual - Manager port.");
			start(name);
		} catch (UnknownHostException uhe) {
			System.out.println("Host unknown: " + uhe.getMessage());
			System.exit(1);
		} catch (IOException ioe) {
			System.out.println("Unexpected exception: " + ioe.getMessage());
			System.exit(1);
		}
	}

	public void run() {
		while (thread != null) {
			try {
				streamOut.writeUTF(console.readLine());
				streamOut.flush();
			} catch (IOException ioe) {
				System.out.println("Sending error: " + ioe.getMessage());
				stop();
			}
		}
	}

	public void handle(String msg) {
		if (msg.equals("disc")) {
			System.out.println("Disconnecting.");
			stop();
		} else System.out.println(msg);
	}

	public void start(String name) throws IOException {
		console = new DataInputStream(System.in);
		streamOut = new DataOutputStream(socket.getOutputStream());
		streamOut.writeUTF("newcom " + name);
		if (thread == null) {
			client = new ManagerSideThread(this, socket);
			thread = new Thread(this);
			thread.start();
		}
	}

	public void stop() {
		if (thread != null) {
			thread.stop();
			thread = null;
		}
		try {
			if (console != null) console.close();
			if (streamOut != null) streamOut.close();
			if (socket != null) socket.close();
		} catch (IOException ioe) {
			System.out.println("Error closing ...");
		}
		client.close();
		client.stop();
	}
}