package me.joaogl.server.data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ServerLogger {
	static BufferedWriter outF = null;

	public static void print(String message) {
		System.out.print(message);
		logger(message);
	}

	public static void println(String message) {
		System.out.println(message);
		logger(message);
	}

	public static void printErrors(String message) {
		System.out.println(message);
		errorLogger(message);
	}

	public static void logger(String message) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("server.log", true)));
			out.println(message);
			out.close();
		} catch (IOException e) {
			printErrors(e.getMessage());
		}
	}

	public static void errorLogger(String message) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("ERROR.log", true)));
			out.println(message);
			out.close();
		} catch (IOException e) {
			printErrors(e.getMessage());
		}
	}
}
