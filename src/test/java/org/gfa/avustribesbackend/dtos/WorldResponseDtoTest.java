package org.gfa.avustribesbackend.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorldResponseDtoTest {
  @Test
  void creation_of_current_DTO() {
    // Arrange
    long id = 1;
    String name = "Drinok";
    long kingdom_count = 3;

    // Act
    WorldResponseDto worldResponseDto = new WorldResponseDto(id, name, kingdom_count);

    // Assert
    assertNotNull(worldResponseDto);
    assertEquals(id, worldResponseDto.getId());
    assertEquals(name, worldResponseDto.getName());
    assertEquals(kingdom_count, worldResponseDto.getKingdomCount());
  }
}
