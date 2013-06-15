package me.joaogl.server.sockets;

import java.net.*;
import java.io.*;

import me.joaogl.server.data.DataManager;
import me.joaogl.server.data.ProgramInfo;

public class Server implements Runnable {
	private ServerThread clients[] = new ServerThread[50];
	private ServerSocket server = null;
	private Thread thread = null;
	private int clientCount = 0;

	public Server(int port) {
		try {
			System.out.println("Binding to port " + port + ", please wait  ...");
			server = new ServerSocket(port);
			System.out.println("Server started: " + server.getInetAddress());
			start();
			DataManager.setupRegList();
		} catch (IOException ioe) {
			System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
		}
	}

	public void run() {
		while (thread != null) {
			try {
				addThread(server.accept());
			} catch (IOException ioe) {
				System.out.println("Server accept error: " + ioe);
				stop();
			}
		}
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void stop() {
		if (thread != null) {
			thread.stop();
			thread = null;
		}
	}

	private int findClient(int ID) {
		for (int i = 0; i < clientCount; i++)
			if (clients[i].getID() == ID) return i;
		return -1;
	}

	public synchronized void handle(int ID, String input) {
		if (input.equals("disc")) {
			clients[findClient(ID)].send("disc");
			remove(ID);
		} else for (int i = 0; i < clientCount; i++)
			clients[i].send(ProgramInfo.connectedPilots[ID] + ": " + input);
	}

	public synchronized void remove(int ID) {
		int pos = findClient(ID);
		if (pos >= 0) {
			ServerThread toTerminate = clients[pos];
			System.out.println("Removing client thread " + ID + " at " + pos);
			if (pos < clientCount - 1) for (int i = pos + 1; i < clientCount; i++)
				clients[i - 1] = clients[i];
			clientCount--;
			try {
				toTerminate.close();
			} catch (IOException ioe) {
				System.out.println("Error closing thread: " + ioe);
			}
			try {
				if (toTerminate.isAlive()) toTerminate.stop();
			} catch (final ThreadDeath ex) {
				ex.getStackTrace();
			}
		}
	}

	private void addThread(Socket socket) {
		if (clientCount < clients.length) {
			String[] name = null;
			try {
				DataInputStream streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				String inputLine;

				while ((inputLine = streamIn.readUTF()) != null) {
					if (inputLine.contains("newcom ")) {
						name = inputLine.split(" ");
						if (name.length > 1 && name[1] != null) {
							ProgramInfo.connectedPilots[socket.getPort()] = name[1];
							break;
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("Client " + socket.getInetAddress() + " as connected with the PilotID " + name[1]);
			clients[clientCount] = new ServerThread(this, socket);
			try {
				clients[clientCount].open();
				clients[clientCount].start();
				clientCount++;
			} catch (IOException ioe) {
				System.out.println("Error opening thread: " + ioe);
			}
		} else System.out.println("Client refused: maximum " + clients.length + " reached.");
	}

}