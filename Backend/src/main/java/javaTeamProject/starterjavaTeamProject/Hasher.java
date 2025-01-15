package javaTeamProject.starterjavaTeamProject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * Утилитный класс для хеширования данных и генерации соли с использованием алгоритмов SHA-256.
 */
public final class Hasher {

  /**
   * Генерирует хеш строки с использованием алгоритма SHA-256.
   *
   * @param input строка для хеширования.
   * @return хеш в виде строки в формате HEX.
   * @throws NoSuchAlgorithmException если алгоритм хеширования недоступен.
   */
  public static String getHash(String input) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
    StringBuilder hexString = new StringBuilder();

    for (byte b : hashBytes) {
      String hex = Integer.toHexString(0xff & b);
      if (hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }

    return hexString.toString();
  }

  /**
   * Генерирует соль случайным образом для использования в хешировании.
   *
   * @param rounds количество раундов для генерации соли.
   * @return строка, представляющая соль в формате Base64.
   */
  public static String getSalt(int rounds) {
    byte[] salt = new byte[rounds];
    SecureRandom random = new SecureRandom();
    random.nextBytes(salt);
    return Base64.getEncoder().encodeToString(salt);
  }

  /**
   * Генерирует хеш строки с добавлением соли.
   *
   * @param input строка для хеширования.
   * @param saltRounds количество раундов для генерации соли.
   * @return хеш строки, дополненной солью, в формате "хеш:соль".
   * @throws NoSuchAlgorithmException если алгоритм хеширования недоступен.
   */
  public static String getHash(String input, int saltRounds) throws NoSuchAlgorithmException {
    return Hasher.getHash(input) + ":" + Hasher.getSalt(saltRounds);
  }

  /**
   * Сравнивает оригинальный хеш с хешированным значением строки.
   *
   * @param originalHash оригинальный хеш.
   * @param notHashedValue строка для проверки.
   * @return true, если хеши совпадают, иначе false.
   * @throws NoSuchAlgorithmException если алгоритм хеширования недоступен.
   */
  public static boolean compareHash(String originalHash, String notHashedValue) throws NoSuchAlgorithmException {
    String[] parts = originalHash.split(":");
    if (parts.length != 2) {
      return false;
    }
    String hash = parts[0];
    return hash.equals(Hasher.getHash(notHashedValue));
  }
}


// Для запуска javadoc -d docs Hasher.java
//gradle javadoc - для запуска через gradle 
//mvn javadoc:javadoc - для запуска через maven