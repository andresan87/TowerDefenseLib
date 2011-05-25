package br.com.jera.towerdefenselib;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

import javax.crypto.Cipher;

import android.content.Context;
import android.content.res.AssetManager;

public class StringEncrypter {
	
	public static byte[] readKeyFromFile(Context context, String fileName) {
		AssetManager assets = context.getAssets();
		ArrayList<Byte> byteList = new ArrayList<Byte>();
		InputStream ss = null;
		try {
			ss = assets.open(fileName);
			int value;
			do {
				value = ss.read();
				if (value != -1) {
					byteList.add(((byte) value));
				}
			} while (value != -1);
		} catch (IOException e) {
			// TODO
		} finally {
			try {
				ss.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		byte[] bytes = new byte[byteList.size()];
		for (int t = 0; t < byteList.size(); t++) {
			bytes[t] = byteList.get(t);
		}
		return bytes;
	}

	public static byte[] encode(byte[] keyBytes, String text) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(text.getBytes());
	}

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
