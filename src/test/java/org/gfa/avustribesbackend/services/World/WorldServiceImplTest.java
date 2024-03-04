package org.gfa.avustribesbackend.services.World;

import org.gfa.avustribesbackend.dtos.WorldResponseDto;
import org.gfa.avustribesbackend.exceptions.NotFoundException;
import org.gfa.avustribesbackend.models.Kingdom;
import org.gfa.avustribesbackend.models.World;
import org.gfa.avustribesbackend.repositories.KingdomRepository;
import org.gfa.avustribesbackend.repositories.WorldRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WorldServiceImplTest {
  @InjectMocks private WorldServiceImpl worldService;
  @Mock private KingdomRepository kingdomRepository;
  @Mock private WorldRepository worldRepository;

  @Test
  void find_one_world() {
    // Arrange
    World world = new World();
    world.setId(1L);

    Kingdom kingdom = new Kingdom("Utopia", 40.5, 45.4, null, world);
    kingdom.setId(1L);

    List<World> worlds = new ArrayList<>();
    worlds.add(world);

    WorldResponseDto worldResponseDto =
        new WorldResponseDto(
            world.getId(), world.getName(), kingdomRepository.countAllByWorld_Id(world.getId()));

    List<WorldResponseDto> allDTOs = new ArrayList<>();
    allDTOs.add(worldResponseDto);

    // Act
    when(kingdomRepository.countAllByWorld_Id(world.getId())).thenReturn(kingdom.getId());
    when(worldRepository.findAll()).thenReturn(worlds);
    ResponseEntity<Object> goodResponse =
        new ResponseEntity<>(allDTOs, HttpStatusCode.valueOf(200));

    // Assert
    assertNotNull(worldService.index());
    assertEquals(goodResponse.getStatusCode(), worldService.index().getStatusCode());
    assertEquals(goodResponse.hasBody(), worldService.index().hasBody());
    assertEquals(
        Objects.requireNonNull(goodResponse.getBody()).getClass(),
        Objects.requireNonNull(worldService.index().getBody()).getClass());
  }

  @Test
  void find_no_world() {
    // Arrange
    when(worldRepository.findAll()).thenReturn(new ArrayList<>());

    // Act
    NotFoundException exception = org.junit.jupiter.api.Assertions
            .assertThrows(NotFoundException.class, () -> worldService.index());
    //Assert
    assertEquals("No World!", exception.getMessage());
  }
}
