package org.gfa.avustribesbackend.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerKingdomResponseDTOTest {

  @Test
  void fields_are_not_null_after_creating_object() {
    PlayerKingdomResponseDTO playerKingdomResponseDTO = new PlayerKingdomResponseDTO(
        1L,
        "Kingdom name",
        1L,
        "World name",
        10.0,
        10.0,
        100,
        100,
        1,
        1,
        1,
        1
    );

    assertNotNull(playerKingdomResponseDTO);
    assertNotNull(playerKingdomResponseDTO.getKingdomId());
    assertNotNull(playerKingdomResponseDTO.getKingdomName());
    assertNotNull(playerKingdomResponseDTO.getWorldId());
    assertNotNull(playerKingdomResponseDTO.getWorldName());
    assertNotNull(playerKingdomResponseDTO.getCoordinateX());
    assertNotNull(playerKingdomResponseDTO.getCoordinateY());
    assertNotNull(playerKingdomResponseDTO.getGoldAmount());
    assertNotNull(playerKingdomResponseDTO.getFoodAmount());
    assertNotNull(playerKingdomResponseDTO.getTownHallLevel());
    assertNotNull(playerKingdomResponseDTO.getFarmLevel());
    assertNotNull(playerKingdomResponseDTO.getMineLevel());
    assertNotNull(playerKingdomResponseDTO.getAcademyLevel());
  }
}
