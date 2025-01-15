package javaTeamProject.starterjavaTeamProject;

import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import io.github.cdimascio.dotenv.Dotenv;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Objects;

/**
 * Утилитный класс для шифрования и расшифровки данных с использованием алгоритма AES.
 * Ключ шифрования загружается из переменных окружения с помощью библиотеки dotenv.
 */
public class AesEncryptor {

  /**
   * Алгоритм шифрования AES.
   */
  private static final String ALGORITHM = "AES";

  /**
   * Экземпляр dotenv для загрузки переменных окружения.
   */
  private static final Dotenv dotenv = Dotenv.load();

  /**
   * Ключ шифрования AES, загружаемый из переменной окружения "AES_KEY".
   */
  private static final byte[] KEY = Objects.requireNonNull(dotenv.get("AES_KEY")).getBytes();

  /**
   * Шифрует переданные данные с использованием AES.
   *
   * @param data строка с данными, которые необходимо зашифровать.
   * @return зашифрованные данные в формате Base64.
   * @throws Exception если произошла ошибка при шифровании.
   */
  public static String encrypt(String data) throws Exception {
    SecretKeySpec secretKey = new SecretKeySpec(KEY, ALGORITHM);
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] encryptedBytes = cipher.doFinal(data.getBytes());
    return Base64.getEncoder().encodeToString(encryptedBytes);
  }

  /**
   * Расшифровывает переданные зашифрованные данные с использованием AES.
   *
   * @param encryptedData зашифрованные данные в формате Base64.
   * @return расшифрованные данные в виде строки.
   * @throws Exception если произошла ошибка при расшифровке.
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

// Для запуска javadoc -d docs AesEncryptor.java
//gradle javadoc - для запуска через gradle 
//mvn javadoc:javadoc - для запуска через maven