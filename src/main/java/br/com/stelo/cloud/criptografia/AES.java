package br.com.stelo.cloud.criptografia;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import br.com.stelo.cloud.constant.Constants;
import br.com.stelo.cloud.utils.PropertiesUtils;

/**
 * Classe que fornece os servicos de criptografia AES.
 * 
 * @author felipeb
 *
 */
public class AES {

	/**
	 * Encrypt.
	 *
	 * @param plainText
	 *            the plain text
	 * @param encryptionKey
	 *            the encryption key
	 * @return the byte[]
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws ErroInesperadoException
	 *             the erro inesperado exception
	 */
	public static byte[] encrypt(final String plainText)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			UnsupportedEncodingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {

		byte[] dataToSend = plainText.getBytes();
		Cipher c = null;
		c = Cipher.getInstance("AES");
		SecretKeySpec k = new SecretKeySpec(PropertiesUtils.getMessage(
				Constants.ENCONDE_KEY).getBytes(), "AES");
		c.init(Cipher.ENCRYPT_MODE, k);
		byte[] encryptedData = "".getBytes();
		encryptedData = c.doFinal(dataToSend);
		byte[] encryptedByteValue = new Base64().encode(encryptedData);
		return encryptedByteValue;
	}

	/**
	 * Decrypt.
	 *
	 * @param cipherText
	 *            the cipher text
	 * @param encryptionKey
	 *            the encryption key
	 * @return the string
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws ErroInesperadoException
	 *             the erro inesperado exception
	 */
	public static String decrypt(final byte[] cipherText,
			final String encryptionKey) throws NoSuchAlgorithmException,
			NoSuchPaddingException, UnsupportedEncodingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		byte[] encryptedData = new Base64().decode(cipherText);
		Cipher c = null;
		c = Cipher.getInstance("AES");
		SecretKeySpec k = new SecretKeySpec(encryptionKey.getBytes(), "AES");
		c.init(Cipher.DECRYPT_MODE, k);
		byte[] decrypted = null;
		decrypted = c.doFinal(encryptedData);
		return new String(decrypted);
	}

}
