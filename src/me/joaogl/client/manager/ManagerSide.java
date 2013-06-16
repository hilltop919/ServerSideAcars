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
		int stage = 0;
		String[] input = null;
		Console c = System.console();
		if (c == null) {
			System.err.println("No console.");
			System.exit(1);
		}
		String pw = null;

		System.out.println("Type connect username - to connect");
		while (running) {
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			String fromUser = null;
			try {
				fromUser = stdIn.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (fromUser != null && fromUser.contains("connect") && stage == 0) {
				if (fromUser.contains(" ")) {
					input = fromUser.split(" ");
					if (input.length > 1 && input[1] != null) {
						ManagerDataEcrypter.ecrypt(input[1]);
						stage = 1;
					} else System.out.println("Type connect username - to connect");
				} else System.out.println("Type connect username - to connect");
			} else System.out.println("You are not connected");
			if (stage == 1) {
				char[] password = null;
				boolean right = false;
				while (!right) {
					password = c.readPassword("Enter your password: ");
					if (password.length >= 6) {
						pw = Character.toString(password[0]);
						for (int i = 1; i < password.length; i++)
							pw += Character.toString(password[i]);
						if (pw.contains(" ")) System.out.println("Incorrect password.");
						else right = true;
					} else System.out.println("Incorrect password.");
				}
				System.out.println("Connecting as administrator " + input[1]);
				ManagerSide manager = new ManagerSide(24467, input[1], pw);
				running = false;
			}
		}
	}

	public ManagerSide(int serverPort, String name, String password) {
		System.out.println("Establishing connection. Please wait ...");
		try {
			socket = new Socket("localhost", serverPort);
			System.out.println("Connected to the Aircraft Management Server - Rio Sul Virtual - Manager port.");
			start(name, password);
		} catch (UnknownHostException uhe) {
			System.out.println("Host unknown: " + uhe.getMessage());
			System.exit(1);
		} catch (IOException ioe) {
			if (ioe.getMessage().contains("Connection refused: ")) System.out.println("Server is not available.");
			else System.out.println("Unexpected exception: " + ioe.getMessage());
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

	public void start(String name, String password) throws IOException {
		console = new DataInputStream(System.in);
		streamOut = new DataOutputStream(socket.getOutputStream());
		streamOut.writeUTF("newmancom " + name + " " + password);
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