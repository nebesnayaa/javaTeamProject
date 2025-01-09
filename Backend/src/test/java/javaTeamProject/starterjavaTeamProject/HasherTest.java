package javaTeamProject.starterjavaTeamProject;

import io.vertx.junit5.VertxExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@ExtendWith(VertxExtension.class)
public class HasherTest {
  @Test
  void compareHashTest() throws NoSuchAlgorithmException {
    String str = "testString";
    String hash = Hasher.getHash(str, 10);
    assertTrue(Hasher.compareHash(hash, str), "The hash should match the original string.");

    String differentStr = "differentString";
    assertFalse(Hasher.compareHash(hash, differentStr), "The hash should not match a different string.");
  }
}
