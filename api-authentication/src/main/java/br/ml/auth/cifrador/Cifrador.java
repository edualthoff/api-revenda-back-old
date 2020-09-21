package br.ml.auth.cifrador;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Cifrador {

	private static String chaveSimetrica = "k45*#:[N2$[rEe&U";
	private static SecretKey key;
	private static byte[] mensagemEncriptada;
	private static byte[] mensagemDescriptada;
	
	public byte[] criptografar(String mensagem) {
		try {
			key = new SecretKeySpec(chaveSimetrica.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			mensagemEncriptada = cipher.doFinal(mensagem.getBytes());
			return mensagemEncriptada;
			
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String descriptografar(byte[] mensagemEncriptada) {
		try {
			key = new SecretKeySpec(chaveSimetrica.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			mensagemDescriptada = cipher.doFinal(mensagemEncriptada);
			return new String(mensagemDescriptada);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
