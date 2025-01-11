package javaTeamProject.starterjavaTeamProject;

import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import io.github.cdimascio.dotenv.Dotenv;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Objects;

public class AesEncryptor {
  private static final String ALGORITHM = "AES";
  private static final Dotenv dotenv = Dotenv.load();
  private static final byte[] KEY = Objects.requireNonNull(dotenv.get("AES_KEY")).getBytes();

  public static String encrypt(String data) throws Exception {
    SecretKeySpec secretKey = new SecretKeySpec(KEY, ALGORITHM);
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] encryptedBytes = cipher.doFinal(data.getBytes());
    return Base64.getEncoder().encodeToString(encryptedBytes);
  }

  public static String decrypt(String encryptedData) throws Exception {
    SecretKeySpec secretKey = new SecretKeySpec(KEY, ALGORITHM);
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
    byte[] decryptedBytes = cipher.doFinal(decodedBytes);
    return new String(decryptedBytes);
  }
}
