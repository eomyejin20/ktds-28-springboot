package com.ktdsuniversity.edu.commons.crypto;

import com.ktdsuniversity.edu.commons.crypto.encrypt.hash.SHA;

public class Test {

	private static final String AES_SECRET_KEY = "abcde12345abcde12345abcde12345zz";
	
	public static void testSHA() {
		String rawPassword = "password1234";
		
		// SHA를 이용한 이중 암호화
		// 1. 이중 암호화를 위한 SALT 발급.
//		String salt = SHA.generateSalt();
		String salt = "5673d55a54608db9";
		System.out.println(salt);
		
		// 2. rawPassword와 SALT를 이용한 암호화
		String encryptedPassword = SHA.getEncrypt(rawPassword, salt);
		System.out.println(encryptedPassword);
	}
	
	public static void testAESEnc() {
		
		// AES 암호화
		String name = "장민창";
		String ecryptedName = AES.encode(AES_SECRET_KEY, name);
		System.out.println(ecryptedName);
	}
	
	public static void testAESDec() {
		// AES 복호화
		String encryptedName = "3856575d0c580ef158fc673aba4a3039";
		String rawName = AES.decode(AES_SECRET_KEY, encryptedName);
		System.out.println(rawName);
	}
	
	public static void main(String[] args) {
		testSHA();
		testAESEnc();
		testAESDec();
	}
	
}