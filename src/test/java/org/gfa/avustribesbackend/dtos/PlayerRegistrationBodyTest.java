package org.gfa.avustribesbackend.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerRegistrationBodyTest {

  @Test
  void creation_of_current_DTO() {
    // Arrange
    String username = "Candylover";
    String email = "Candy@gmail.com";
    String password = "password";

    // Act
    PlayerRegistrationBody playerRegistrationBody =
        new PlayerRegistrationBody(username, email, password);

    // Assert
    assertNotNull(playerRegistrationBody);
    assertEquals(username, playerRegistrationBody.getUsername());
    assertEquals(email, playerRegistrationBody.getEmail());
    assertEquals(password, playerRegistrationBody.getPassword());
  }
}
