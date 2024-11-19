package com.backend.auth;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtil {
    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "16ByteSecretKey!"; // 16-byte key for AES-128

    // Encrypts a plaintext password
    public static String encrypt(String plainText) {
        try {
            SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes); // Encode as Base64
        } catch (Exception e) {
            throw new RuntimeException("Error during encryption", e);
        }
    }

    // Decrypts an encrypted password
    public static String decrypt(String encryptedText) {
        try {
            SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decryptedBytes); // Convert bytes to String
        } catch (Exception e) {
            throw new RuntimeException("Error during decryption", e);
        }
    }
}
