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
import me.joaogl.server.data.ServerLogger;

public class ServerThread extends Thread {
	private Server server = null;
	private Socket socket = null;
	private String pw = null;
	private int ID = -1;
	private DataInputStream streamIn = null;
	private DataOutputStream streamOut = null;

	public ServerThread(Server _server, Socket _socket, String _pw, String _id) throws IOException {
		super();
		server = _server;
		socket = _socket;
		pw = _pw;
		ID = socket.getPort();
		if (!DataManager.getPilot(_id, pw)) {
			close();
			server.remove(ID);
			stop();
		}
	}

	public void send(String msg) {
		try {
			ServerLogger.println(msg);
			streamOut.writeUTF(msg);
			streamOut.flush();
		} catch (IOException ioe) {
			ServerLogger.println(ID + " ERROR sending: " + ioe.getMessage());
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
				ServerLogger.println(ID + " ERROR reading: " + ioe.getMessage());
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