package org.gfa.avustribesbackend.models;

import org.gfa.avustribesbackend.models.enums.BuildingTypeValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildingTest {

  Building building;

  @BeforeEach
  void setup() {
    building = new Building(new Kingdom(), BuildingTypeValue.FARM);
  }

  @Test
  void all_values_are_set_correctly() {
    int expectedLevel = 1;

    assertNotNull(building.getType());
    assertNotNull(building.getKingdom());
    assertEquals(building.getLevel(), expectedLevel);
  }

  @Test
  void level_is_set_to_1_by_default() {
    building = new Building();

    int expectedLevel = 1;

    assertEquals(building.getLevel(), expectedLevel);
  }

  @Test
  void throws_exception_when_level_set_to_negative() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> building.setLevel(-5));

    String expectedMessage = "Level cannot be less than 1";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }
}
