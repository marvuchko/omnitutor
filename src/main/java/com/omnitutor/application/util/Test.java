package com.omnitutor.application.util;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Test {

	public static void main(String[] args) {
		try {
			System.out.println(GoogleAuthUtil.authorize(RequestUtil.GOOGLE_DRIVE_AUTH).getAccessToken());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
