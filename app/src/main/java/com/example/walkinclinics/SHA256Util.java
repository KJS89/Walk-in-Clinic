
package com.example.walkinclinics;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

public class SHA256Util {


	public static String encrypt(String pwd) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] hash = md.digest(pwd.getBytes(StandardCharsets.UTF_8));
		StringBuilder stringbuffer = new StringBuilder();
		for (byte b : hash) {
			stringbuffer.append(String.format("%02x", b));
			stringbuffer.append(":");
		}
		return stringbuffer.toString().substring(0, 95).toUpperCase();
	}
}

