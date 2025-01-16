package javaTeamProject.starterjavaTeamProject;

import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import io.github.cdimascio.dotenv.Dotenv;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Objects;

/**
 * Utility class for encrypting and decrypting data using the AES algorithm.
 * The encryption key is loaded from environment variables using the dotenv library.
 */
public class AesEncryptor {

  /**
   * AES encryption algorithm.
   */
  private static final String ALGORITHM = "AES";

  /**
   * Dotenv instance for loading environment variables.
   */
  private static final Dotenv dotenv = Dotenv.load();

  /**
   * AES encryption key, loaded from the environment variable "AES_KEY".
   */
  private static final byte[] KEY = Objects.requireNonNull(dotenv.get("AES_KEY")).getBytes();

  /**
   * Encrypts the provided data using AES.
   *
   * @param data the string containing the data to be encrypted.
   * @return the encrypted data in Base64 format.
   * @throws Exception if an error occurs during encryption.
   */
  public static String encrypt(String data) throws Exception {
    SecretKeySpec secretKey = new SecretKeySpec(KEY, ALGORITHM);
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] encryptedBytes = cipher.doFinal(data.getBytes());
    return Base64.getEncoder().encodeToString(encryptedBytes);
  }

  /**
   * Decrypts the provided encrypted data using AES.
   *
   * @param encryptedData the encrypted data in Base64 format.
   * @return the decrypted data as a string.
   * @throws Exception if an error occurs during decryption.
   */
  public static String decrypt(String encryptedData) throws Exception {
    SecretKeySpec secretKey = new SecretKeySpec(KEY, ALGORITHM);
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
    byte[] decryptedBytes = cipher.doFinal(decodedBytes);
    return new String(decryptedBytes);
  }
}
