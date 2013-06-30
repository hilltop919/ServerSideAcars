/**
 * This file is part of ServerSideAcars.

    ServerSideAcars is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ServerSideAcars is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ServerSideAcars.  If not, see <http://www.gnu.org/licenses/>.
 */


package me.joaogl.server.sockets;

import java.net.*;
import java.io.*;

import me.joaogl.server.data.DataManager;
import me.joaogl.server.data.ProgramInfo;

public class Server implements Runnable {
	private static ServerThread clients[] = new ServerThread[50];
	private ServerSocket server = null;
	private Thread thread = null;
	private static int clientCount = 0;

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
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static int findClient(int ID) {
		for (int i = 0; i < clientCount; i++)
			if (clients[i].getID() == ID) return i;
		return -1;
	}

	public synchronized void handle(int ID, String input) {
		if (input.equals("disc")) {
			clients[findClient(ID)].send("disc");
			remove(ID);
		} else {
			if (ProgramInfo.connectedPilots[ID] != null) clients[findClient(ID)].send(ProgramInfo.connectedPilots[ID] + ": " + input);
			else clients[findClient(ID)].send(ProgramInfo.connectedManagers[ID] + ": " + input);
		}
	}

	public synchronized void handleForAll(int ID, String input) {
		if (input.equals("disc")) {
			clients[findClient(ID)].send("disc");
			remove(ID);
		} else for (int i = 0; i < clientCount; i++) {
			if (ProgramInfo.connectedPilots[ID] != null) clients[i].send(ProgramInfo.connectedPilots[ID] + ": " + input);
			else clients[i].send(ProgramInfo.connectedManagers[ID] + ": " + input);
		}
	}

	public static synchronized void remove(int ID) {
		int pos = findClient(ID);
		if (pos >= 0) {
			ServerThread toTerminate = clients[pos];
			if (ProgramInfo.connectedPilots[ID] != null) System.out.println("Removing client " + ProgramInfo.connectedPilots[ID] + ".");
			else System.out.println("Removing Manager " + ProgramInfo.connectedManagers[ID] + ".");
			if (pos < clientCount - 1) for (int i = pos + 1; i < clientCount; i++)
				clients[i - 1] = clients[i];
			clientCount--;
			try {
				toTerminate.close();
				toTerminate.stop();
			} catch (IOException ioe) {
				System.out.println("Error closing thread: " + ioe);
			}
		}
	}

	private void addThread(Socket socket) {
		boolean accepted = false;
		if (clientCount < clients.length) {
			String[] name = new String[3];
			try {
				DataInputStream streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				String inputLine;

				while ((inputLine = streamIn.readUTF()) != null) {
					if (inputLine.contains("newcom ") || inputLine.contains("newmancom ")) {
						name = inputLine.split(" ");
						if (name.length >= 3) {
							if (name[1] != null && name[2] != null) {
								if (inputLine.contains("newcom ")) ProgramInfo.connectedPilots[socket.getPort()] = name[1];
								else if (inputLine.contains("newmancom ")) ProgramInfo.connectedManagers[socket.getPort()] = name[1];
								accepted = true;
								break;
							}
						} else {
							System.out.println("Hacked client trying to join: " + socket.getInetAddress());
							if (socket != null) socket.close();
							if (streamIn != null) streamIn.close();
						}
					}
				}
			} catch (IOException e) {
				if (e.getMessage().contains("Stream closed")) System.out.println("Hacked client connection ended.");
				else e.printStackTrace();
			}

			if (accepted) {
				if (ProgramInfo.connectedPilots[socket.getPort()] == name[1]) System.out.println("Client " + socket.getInetAddress() + " as connected with the PilotID " + name[1]);
				else System.out.println("Manager " + socket.getInetAddress() + " as connected with the name " + name[1]);
				try {
					clients[clientCount] = new ServerThread(this, socket, name[2], name[1]);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					clients[clientCount].open();
					clients[clientCount].start();
					clientCount++;
				} catch (IOException ioe) {
					System.out.println("Error opening thread: " + ioe);
				}
			}
			accepted = false;
		} else System.out.println("Client refused: maximum " + clients.length + " reached.");
	}

}