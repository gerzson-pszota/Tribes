package org.gfa.avustribesbackend.services.Building;

import org.gfa.avustribesbackend.dtos.BuildNewBuildingDTO;
import org.gfa.avustribesbackend.exceptions.BuildingException;
import org.gfa.avustribesbackend.models.Building;
import org.gfa.avustribesbackend.models.Kingdom;
import org.gfa.avustribesbackend.models.Resource;
import org.gfa.avustribesbackend.models.enums.BuildingTypeValue;
import org.gfa.avustribesbackend.models.enums.ResourceTypeValue;
import org.gfa.avustribesbackend.repositories.BuildingRepository;
import org.gfa.avustribesbackend.repositories.KingdomRepository;
import org.gfa.avustribesbackend.repositories.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class BuildingServiceImplTest {
  @Mock private BuildingService buildingService;
  @Mock BuildingRepository buildingRepository;
  @Mock KingdomRepository kingdomRepository;
  @Mock ResourceRepository resourceRepository;
  @InjectMocks private BuildingServiceImpl buildingServiceImpl;

  @BeforeEach
  public void beforeEach() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void building_without_townhall_throws_exception() {
    // Arrange
    Kingdom kingdom = new Kingdom();
    kingdom.setId(1L); // Set kingdom ID
    kingdom.setResources(new ArrayList<>()); // Initialize resources list
    when(kingdomRepository.findById(any()))
        .thenReturn(Optional.of(kingdom)); // Simulate kingdom found
    when(buildingRepository.findByKingdomAndType(any(), eq(BuildingTypeValue.TOWNHALL)))
        .thenReturn(null); // Simulate no townhall in the kingdom

    BuildNewBuildingDTO dto = new BuildNewBuildingDTO(1L, BuildingTypeValue.FARM);

    // Act & Assert
    BuildingException exception =
        assertThrows(BuildingException.class, () -> buildingServiceImpl.buildNewBuilding(dto));
    assertEquals("You need to build a townhall first!", exception.getMessage());
  }

  @Test
  void building_without_money_throws_exception() {
    // Arrange
    Kingdom kingdom = new Kingdom();
    when(kingdomRepository.findById(any())).thenReturn(Optional.of(kingdom));
    when(buildingRepository.findByKingdomAndType(any(), eq(BuildingTypeValue.TOWNHALL)))
        .thenReturn(new Building(kingdom, BuildingTypeValue.TOWNHALL));

    kingdom.setResources(null);

    BuildNewBuildingDTO dto = new BuildNewBuildingDTO(1L, BuildingTypeValue.FARM);

    BuildingException exception =
        assertThrows(BuildingException.class, () -> buildingServiceImpl.buildNewBuilding(dto));
    assertEquals("You don't have enough gold!", exception.getMessage());
  }

  @Test
  void building_successful_returns_true() {
    // Arrange
    Kingdom kingdom = new Kingdom();
    when(kingdomRepository.findById(any())).thenReturn(Optional.of(kingdom));
    when(buildingRepository.findByKingdomAndType(any(), eq(BuildingTypeValue.TOWNHALL)))
        .thenReturn(new Building(kingdom, BuildingTypeValue.TOWNHALL));

    List<Resource> resources = new ArrayList<>();
    resources.add(new Resource(kingdom, ResourceTypeValue.GOLD, 1000)); // Enough gold
    kingdom.setResources(resources);

    BuildNewBuildingDTO dto = new BuildNewBuildingDTO(1L, BuildingTypeValue.FARM);
    when(buildingService.buildNewBuilding(any())).thenReturn(true);

    // Act
    boolean buildingResult = buildingServiceImpl.buildNewBuilding(dto);

    // Assert
    assertTrue(buildingResult);
  }
}
