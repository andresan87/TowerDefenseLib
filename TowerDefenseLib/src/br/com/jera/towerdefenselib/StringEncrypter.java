package br.com.jera.towerdefenselib;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringEncrypter {

	public static String encodeSHA(String str) {
		String r = "";
		try {
			MessageDigest md;
			md = MessageDigest.getInstance("SHA");
			md.update(str.getBytes());  
			BigInteger hash = new BigInteger( 1, md.digest() );  
			r = hash.toString(16);  
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return r;
	}
}
