package me.joaogl.client.manager;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ManagerDataEcrypter {

	public static void ecrypt(String message) {
		byte[] bytes = message.getBytes();
		StringBuilder binary = new StringBuilder();
		for (byte b : bytes) {
			int val = b;
			for (int i = 0; i < 8; i++) {
				binary.append((val & 128) == 0 ? 0 : 1);
				val <<= 1;
			}
			binary.append(' ');
		}
		System.out.println("'" + message + "' to binary: " + binary);
	}

	public static String ecryptpw(String pw) {
		MessageDigest md = null;
		byte[] bytesOfMessage = null;
		try {
			bytesOfMessage = pw.getBytes("UTF-8");
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return (md.digest(bytesOfMessage).toString());
	}
}