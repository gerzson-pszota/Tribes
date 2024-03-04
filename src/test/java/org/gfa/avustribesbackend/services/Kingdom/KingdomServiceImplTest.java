package org.gfa.avustribesbackend.services.Kingdom;

import org.gfa.avustribesbackend.controllers.KingdomController;
import org.gfa.avustribesbackend.dtos.KingdomResponseDTO;
import org.gfa.avustribesbackend.dtos.PlayerKingdomResponseDTO;
import org.gfa.avustribesbackend.exceptions.CredentialException;
import org.gfa.avustribesbackend.models.*;
import org.gfa.avustribesbackend.repositories.KingdomRepository;
import org.gfa.avustribesbackend.repositories.PlayerRepository;
import org.gfa.avustribesbackend.services.JWT.JwtServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class KingdomServiceImplTest {
  @InjectMocks private KingdomController kingdomController;

  @Mock private KingdomService kingdomService;

  @InjectMocks private KingdomServiceImpl kingdomServiceImpl;

  @Mock private KingdomRepository kingdomRepository;

  @Mock JwtServiceImpl jwtService;

  @Mock PlayerRepository playerRepository;

  @BeforeEach
  public void beforeEach() {
    KingdomResponseDTO kingdomResponseDTO =
        new KingdomResponseDTO(1L, 1L, 1L, "testKingdom", 10.0, 20.0);
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void list_all_kingdoms() {
    Kingdom kingdom1 = new Kingdom();
    kingdom1.setId(1L);
    kingdom1.setName("Kingdom1");
    kingdom1.setCoordinateX(10.0);
    kingdom1.setCoordinateY(20.0);

    World world1 = new World();
    world1.setId(1L);
    kingdom1.setWorld(world1);

    Player player1 = new Player();
    player1.setId(1L);
    kingdom1.setPlayer(player1);

    Kingdom kingdom2 = new Kingdom();
    kingdom2.setId(2L);
    kingdom2.setName("Kingdom2");
    kingdom2.setCoordinateX(15.0);
    kingdom2.setCoordinateY(25.0);

    World world2 = new World();
    world2.setId(2L);
    kingdom2.setWorld(world2);

    Player player2 = new Player();
    player2.setId(2L);
    kingdom2.setPlayer(player2);

    when(kingdomRepository.findAll()).thenReturn(Arrays.asList(kingdom1, kingdom2));

    when(kingdomService.listKingdoms())
        .thenReturn(
            Arrays.asList(
                kingdomServiceImpl.createKingdomDTO(kingdom1),
                kingdomServiceImpl.createKingdomDTO(kingdom2)));

    List<KingdomResponseDTO> list1 = new ArrayList<>();
    list1.add(kingdomServiceImpl.createKingdomDTO(kingdom1));
    list1.add(kingdomServiceImpl.createKingdomDTO(kingdom2));

    List<KingdomResponseDTO> list2 = kingdomServiceImpl.listKingdoms();

    assertEquals(list1.size(), list2.size());
  }

  @Test
  void show_one_kingdom_found_returns_DTO() {
    // Arrange
    long kingdomId = 1L;
    KingdomResponseDTO kingdomResponseDTO =
        new KingdomResponseDTO(kingdomId, 1L, 1L, "test", 10.0, 10.0);

    // Mocking behavior
    when(kingdomService.checkId(kingdomId)).thenReturn(true);
    when(kingdomService.returnKingdomDTOById(kingdomId)).thenReturn(kingdomResponseDTO);

    // Act
    ResponseEntity<Object> responseEntity = kingdomController.index(kingdomId);

    // Assert
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(kingdomResponseDTO, responseEntity.getBody());
    verify(kingdomService, times(1)).checkId(kingdomId);
    verify(kingdomService, times(1)).returnKingdomDTOById(kingdomId);
  }

  @Test
  void show_one_kingdom_not_found_throws_exception() {
    // Arrange
    long kingdomId = 2L;

    // Mocking behavior
    when(kingdomService.checkId(kingdomId)).thenReturn(false);
    when(kingdomService.returnKingdomDTOById(kingdomId))
        .thenThrow(new CredentialException("Kingdom not found"));

    // Act
    CredentialException exception =
        assertThrows(CredentialException.class, () -> kingdomController.index(kingdomId));

    // Assert
    assertEquals("Kingdom not found", exception.getMessage());
  }

  @Test
  void listPlayerKingdoms_should_return_list() {
    List<PlayerKingdomResponseDTO> expected = new ArrayList<>();

    List<Kingdom> kingdoms = getKingdoms();

    for (Kingdom kingdom : kingdoms) {
      PlayerKingdomResponseDTO dto = kingdomServiceImpl.getPlayerKingdomResponseDTO(kingdom);
      expected.add(dto);
    }

    when(kingdomRepository.findKingdomsByPlayer(any())).thenReturn(kingdoms);

    List<PlayerKingdomResponseDTO> actual = kingdomServiceImpl.listPlayerKingdoms(any());

    assertNotNull(actual);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.get(0).getKingdomId(), actual.get(0).getKingdomId());
  }

  @NotNull
  private static List<Kingdom> getKingdoms() {
    World world = new World();
    world.setId(1L);
    world.setName("World");

    Resource resourceGold1 = new Resource();
    resourceGold1.setAmount(100);

    Resource resourceFood1 = new Resource();
    resourceFood1.setAmount(150);

    List<Resource> resourceList1 = new ArrayList<>(Arrays.asList(resourceGold1, resourceFood1));

    Resource resourceGold2 = new Resource();
    resourceGold2.setAmount(200);

    Resource resourceFood2 = new Resource();
    resourceFood2.setAmount(250);

    List<Resource> resourceList2 = new ArrayList<>(Arrays.asList(resourceGold2, resourceFood2));

    Building townHall1 = new Building();
    townHall1.setLevel(4);

    Building farm1 = new Building();
    farm1.setLevel(3);

    Building mine1 = new Building();
    mine1.setLevel(2);

    Building academy1 = new Building();
    academy1.setLevel(1);

    List<Building> buildingsList1 = new ArrayList<>(Arrays.asList(townHall1, farm1, mine1, academy1));

    Building townHall2 = new Building();
    townHall2.setLevel(8);

    Building farm2 = new Building();
    farm2.setLevel(7);

    Building mine2 = new Building();
    mine2.setLevel(6);

    Building academy2 = new Building();
    academy2.setLevel(5);

    List<Building> buildingsList2 = new ArrayList<>(Arrays.asList(townHall2, farm2, mine2, academy2));

    Kingdom kingdom1 = new Kingdom();
    kingdom1.setId(1L);
    kingdom1.setName("Kingdom1");
    kingdom1.setCoordinateX(10.0);
    kingdom1.setCoordinateY(20.0);
    kingdom1.setBuildings(buildingsList1);
    kingdom1.setResources(resourceList1);
    kingdom1.setWorld(world);

    Kingdom kingdom2 = new Kingdom();
    kingdom2.setId(2L);
    kingdom2.setName("Kingdom2");
    kingdom2.setCoordinateX(15.0);
    kingdom2.setCoordinateY(25.0);
    kingdom2.setBuildings(buildingsList2);
    kingdom2.setResources(resourceList2);
    kingdom2.setWorld(world);

    return new ArrayList<>(Arrays.asList(kingdom1, kingdom2));
  }
}
