package org.gfa.avustribesbackend.services.Troop;

import org.gfa.avustribesbackend.exceptions.CreationException;
import org.gfa.avustribesbackend.models.*;
import org.gfa.avustribesbackend.models.enums.BuildingTypeValue;
import org.gfa.avustribesbackend.models.enums.ResourceTypeValue;
import org.gfa.avustribesbackend.repositories.BuildingRepository;
import org.gfa.avustribesbackend.repositories.ResourceRepository;
import org.gfa.avustribesbackend.repositories.TroopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class TroopServiceImplTest {
  @Mock TroopRepository troopRepository;
  @Mock BuildingRepository buildingRepository;
  @Mock ResourceRepository resourceRepository;
  @InjectMocks TroopServiceImpl troopService;
  Kingdom kingdom;

  @BeforeEach
  public void beforeEach() {
    World world = new World();
    Player player = new Player();
    kingdom = new Kingdom(10.0, 10.0, player, world);
  }

  @Test
  void creating_troop_no_enough_gold() {
    CreationException creationException =
        assertThrows(CreationException.class, () -> troopService.create(kingdom));
    assertEquals("Not enough gold to create troop", creationException.getMessage());
  }

  @Test
  void creating_troop_no_academy() {
    when(resourceRepository.getResourceAmount(kingdom, ResourceTypeValue.GOLD)).thenReturn(1000);
    CreationException creationException =
        assertThrows(CreationException.class, () -> troopService.create(kingdom));
    assertEquals("Need academy to train troops!", creationException.getMessage());
  }

  @Test
  void creating_troop_no_enough_food() {
    when(resourceRepository.getResourceAmount(kingdom, ResourceTypeValue.GOLD)).thenReturn(1000);
    Building building = new Building(kingdom, BuildingTypeValue.ACADEMY);
    List<Building> buildings = new ArrayList<>();
    buildings.add(building);
    kingdom.setBuildings(buildings);
    CreationException creationException =
        assertThrows(CreationException.class, () -> troopService.create(kingdom));
    assertEquals("No enough food to feed another hungry troop", creationException.getMessage());
  }

  @Test
  void upgrading() {
    Resource resource = new Resource(kingdom, ResourceTypeValue.GOLD);
    when(resourceRepository.findByKingdomAndType(kingdom, ResourceTypeValue.GOLD))
        .thenReturn(resource);
    CreationException creationException =
        assertThrows(CreationException.class, () -> troopService.upgrade(kingdom));
    assertEquals("Not enough gold to upgrade troops", creationException.getMessage());
  }
}
