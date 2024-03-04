package org.gfa.avustribesbackend.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordResetTest {

  @Test
  void calling_constructor_with_arguments_should_create_an_instance() {
    Player player = new Player();
    String token = "token";
    PasswordReset passwordReset = new PasswordReset(token, player);

    assertNotNull(passwordReset);
    assertNotNull(passwordReset.getPlayer());
    assertEquals(passwordReset.getPlayer(), player);
    assertNotNull(passwordReset.getToken());
    assertEquals(passwordReset.getToken(), token);
    assertNotNull(passwordReset.getCreatedAt());
    assertNotNull(passwordReset.getExpiresAt());
  }
}
