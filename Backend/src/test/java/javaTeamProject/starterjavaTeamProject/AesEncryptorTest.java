package javaTeamProject.starterjavaTeamProject;

import io.vertx.junit5.VertxExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(VertxExtension.class)
public class AesEncryptorTest {

  @Test
  void EncryptionTest() {
    String originalText = "HelloWorld";

    try {
      String encryptedText = AesEncryptor.encrypt(originalText);
      String decryptedText = AesEncryptor.decrypt(encryptedText);

      assertNotNull(encryptedText, "Encrypted text should not be null");
      assertNotEquals(originalText, encryptedText, "Encrypted text should not be the same as original text");
      assertEquals(originalText, decryptedText, "Decrypted text should match the original text");
    } catch (Exception e) {
      fail("EncryptionTest failed with exception: " + e.getMessage());
    }
  }

  @Test
  void NullInputTest() {
    assertThrows(NullPointerException.class, () -> {
      AesEncryptor.encrypt(null);
    }, "Expected NullPointerException for null input during encryption");

    assertThrows(NullPointerException.class, () -> {
      AesEncryptor.decrypt(null);
    }, "Expected NullPointerException for null input during decryption");
  }
}
