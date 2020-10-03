/**
 * 
 */
package com.frost.documentservice.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.frost.documentservice.exception.EncryptionException;

/**
 * @author jobin
 *
 */
@Service
public class CryptoService {

	@Value("${aes.secret}")
	private String secret;

	private static final String UTF8_ENCODING = "UTF-8";
	private static final String AES_ENCRYPTION_ALGORITHM = "AES";
	private static final String SHA1_MESSAGEDIGEST_ALGORITHM = "SHA-1";
	private static final String AES_TRANSFOMRATION = "AES/ECB/PKCS5Padding";

	private SecretKeySpec getKey() {

		try {

			byte[] key = secret.getBytes(UTF8_ENCODING);
			MessageDigest sha = MessageDigest.getInstance(SHA1_MESSAGEDIGEST_ALGORITHM);
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);

			return new SecretKeySpec(key, AES_ENCRYPTION_ALGORITHM);

		} catch (NoSuchAlgorithmException e) {
			throw new EncryptionException("No Algorithm Found!", e);
		} catch (UnsupportedEncodingException e) {
			throw new EncryptionException("Encoding not supported!", e);
		}
	}

	public String encrypt(String strToEncrypt) {
		try {

			SecretKeySpec secretKey = getKey();
			Cipher cipher = Cipher.getInstance(AES_TRANSFOMRATION);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(UTF8_ENCODING)));

		} catch (Exception e) {
			throw new EncryptionException("Error while encrypting the data! ", e);
		}
	}

	public String decrypt(String strToDecrypt) {

		try {

			SecretKeySpec secretKey = getKey();
			Cipher cipher = Cipher.getInstance(AES_TRANSFOMRATION);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);

			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));

		} catch (Exception e) {
			throw new EncryptionException("Error while decrypting the data! ", e);
		}

	}

}
