package me.joaogl.server;

import me.joaogl.server.sockets.Server;

public class MainMethod {

	public static void main(String[] args) {
		Server server = new Server(24467);
	}
}