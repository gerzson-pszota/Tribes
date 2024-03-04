package org.gfa.avustribesbackend.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailVerificationTest {

  @Test
  void calling_constructor_with_arguments_should_create_an_instance() {
    Player player = new Player();
    String token = "token";
    EmailVerification emailVerification = new EmailVerification(token, player);

    assertNotNull(emailVerification);
    assertNotNull(emailVerification.getPlayer());
    assertEquals(emailVerification.getPlayer(), player);
    assertNotNull(emailVerification.getToken());
    assertEquals(emailVerification.getToken(), token);
    assertNotNull(emailVerification.getCreatedAt());
    assertNotNull(emailVerification.getExpiresAt());
  }
}
