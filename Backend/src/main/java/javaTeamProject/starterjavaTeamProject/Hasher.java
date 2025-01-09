package javaTeamProject.starterjavaTeamProject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public final class Hasher {

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

  public static String getSalt(int rounds){
    byte[] salt = new byte[rounds];
    SecureRandom random = new SecureRandom();
    random.nextBytes(salt);
    return Base64.getEncoder().encodeToString(salt);
  }

  public static String getHash(String input, int saltRounds) throws NoSuchAlgorithmException {
    return Hasher.getHash(input) + ":" + Hasher.getSalt(saltRounds);
  }

  public static boolean compareHash(String originalHash, String notHashedValue) throws NoSuchAlgorithmException {
    String[] parts = originalHash.split(":");
    if (parts.length != 2) {
      return false;
    }
    String hash = parts[0];
    return hash.equals(Hasher.getHash(notHashedValue));
  }

}
