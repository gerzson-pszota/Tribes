package org.gfa.avustribesbackend.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KingdomTest {
  @Test
  void test_create_kingdom_all_fields() {
    // Arrange
    Long worldId = 1L;
    String name = "Test Kingdom";
    Double coordinateX = 10.0;
    Double coordinateY = 20.0;
    Player player = new Player();

    // Act
    Kingdom kingdom = new Kingdom(name, coordinateX, coordinateY, player, new World());

    // Assert
    assertEquals(name, kingdom.getName());
    assertEquals(coordinateX, kingdom.getCoordinateX());
    assertEquals(coordinateY, kingdom.getCoordinateY());
  }

  @Test
  void test_create_kingdom_without_coordinates() {
    // Arrange
    Long worldId = 1L;
    String name = "Test Kingdom";

    // Act
    Kingdom kingdom = new Kingdom(name, null, null, null, null);

    // Assert
    assertEquals(name, kingdom.getName());
    assertNull(kingdom.getCoordinateX());
    assertNull(kingdom.getCoordinateY());
  }
}
