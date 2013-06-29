package me.joaogl.server.sockets;

import java.net.*;
import java.io.*;

import me.joaogl.server.data.DataManager;

public class ServerThread extends Thread {
	private Server server = null;
	private Socket socket = null;
	private String pw = null;
	private int ID = -1;
	private DataInputStream streamIn = null;
	private DataOutputStream streamOut = null;

	public ServerThread(Server _server, Socket _socket, String _pw) {
		super();
		server = _server;
		socket = _socket;
		pw = _pw;
		ID = socket.getPort();
		if (!DataManager.getPilot(pw, pw)) {
			// CLOSE CLIENT CONNECTION
		}
	}

	public void send(String msg) {
		try {
			streamOut.writeUTF(msg);
			streamOut.flush();
		} catch (IOException ioe) {
			System.out.println(ID + " ERROR sending: " + ioe.getMessage());
			server.remove(ID);
			stop();
		}
	}

	public int getID() {
		return ID;
	}

	public void run() {
		while (true) {
			try {
				server.handle(ID, streamIn.readUTF());
			} catch (IOException ioe) {
				System.out.println(ID + " ERROR reading: " + ioe.getMessage());
				server.remove(ID);
				try {
					close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				stop();
			}
		}
	}

	public void open() throws IOException {
		streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	}

	public void close() throws IOException {
		if (socket != null) socket.close();
		if (streamIn != null) streamIn.close();
		if (streamOut != null) streamOut.close();
	}

}