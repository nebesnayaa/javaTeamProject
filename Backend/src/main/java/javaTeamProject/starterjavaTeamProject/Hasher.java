package javaTeamProject.starterjavaTeamProject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * Utility class for hashing data and generating salts using SHA-256 algorithms.
 */
public final class Hasher {

  /**
   * Generates a hash of the string using the SHA-256 algorithm.
   *
   * @param input the string to hash.
   * @return the hash as a HEX string.
   * @throws NoSuchAlgorithmException if the hashing algorithm is not available.
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
   * Generates a salt randomly for use in hashing.
   *
   * @param rounds the number of rounds to generate the salt.
   * @return the salt as a Base64 string.
   */
  public static String getSalt(int rounds) {
    byte[] salt = new byte[rounds];
    SecureRandom random = new SecureRandom();
    random.nextBytes(salt);
    return Base64.getEncoder().encodeToString(salt);
  }

  /**
   * Generates a hash of the string with the addition of salt.
   *
   * @param input the string to hash.
   * @param saltRounds the number of rounds to generate the salt.
   * @return the hash of the string with salt in the format "hash:salt".
   * @throws NoSuchAlgorithmException if the hashing algorithm is not available.
   */
  public static String getHash(String input, int saltRounds) throws NoSuchAlgorithmException {
    return Hasher.getHash(input) + ":" + Hasher.getSalt(saltRounds);
  }

  /**
   * Compares the original hash with the hashed value of a string.
   *
   * @param originalHash the original hash.
   * @param notHashedValue the string to check.
   * @return true if the hashes match, false otherwise.
   * @throws NoSuchAlgorithmException if the hashing algorithm is not available.
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
