package org.gfa.avustribesbackend.services.Kingdom;

import jakarta.servlet.http.HttpServletRequest;
import org.gfa.avustribesbackend.dtos.KingdomResponseDTO;
import org.gfa.avustribesbackend.dtos.PlayerKingdomResponseDTO;
import org.gfa.avustribesbackend.exceptions.CredentialException;
import org.gfa.avustribesbackend.models.Building;
import org.gfa.avustribesbackend.models.Kingdom;
import org.gfa.avustribesbackend.models.Player;
import org.gfa.avustribesbackend.models.Resource;
import org.gfa.avustribesbackend.models.*;
import org.gfa.avustribesbackend.models.enums.BuildingTypeValue;
import org.gfa.avustribesbackend.models.enums.ResourceTypeValue;
import org.gfa.avustribesbackend.repositories.BuildingRepository;
import org.gfa.avustribesbackend.repositories.KingdomRepository;
import org.gfa.avustribesbackend.repositories.PlayerRepository;
import org.gfa.avustribesbackend.repositories.ResourceRepository;
import org.gfa.avustribesbackend.services.JWT.JwtService;
import org.gfa.avustribesbackend.repositories.WorldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class KingdomServiceImpl implements KingdomService {
  private final KingdomRepository kingdomRepository;
  private final BuildingRepository buildingRepository;
  private final ResourceRepository resourceRepository;
  private final JwtService jwtService;
  private final PlayerRepository playerRepository;
  private final WorldRepository worldRepository;

  @Autowired
  public KingdomServiceImpl(
      KingdomRepository kingdomRepository,
      BuildingRepository buildingRepository,
      ResourceRepository resourceRepository,
      JwtService jwtService,
      PlayerRepository playerRepository,
      WorldRepository worldRepository) {
    this.kingdomRepository = kingdomRepository;
    this.buildingRepository = buildingRepository;
    this.resourceRepository = resourceRepository;
    this.jwtService = jwtService;
    this.playerRepository = playerRepository;
    this.worldRepository = worldRepository;
  }

  @Override
  public KingdomResponseDTO createKingdomDTO(Kingdom kingdom) {
    return new KingdomResponseDTO(
        kingdom.getId(),
        kingdom.getWorld().getId(),
        kingdom.getPlayer().getId(),
        kingdom.getName(),
        kingdom.getCoordinateX(),
        kingdom.getCoordinateY());
  }

  @Override
  public List<KingdomResponseDTO> listKingdoms() {
    List<Kingdom> allPlayers = kingdomRepository.findAll();
    List<KingdomResponseDTO> dtoList = new ArrayList<>();
    for (Kingdom kingdom : allPlayers) {
      KingdomResponseDTO dto = createKingdomDTO(kingdom);
      dtoList.add(dto);
    }
    return dtoList;
  }

  @Override
  public List<PlayerKingdomResponseDTO> listPlayerKingdoms(HttpServletRequest request) {
    List<PlayerKingdomResponseDTO> dtoList = new ArrayList<>();
    String email = jwtService.extractEmailFromToken(request);
    Player player = playerRepository.findByEmailIgnoreCase(email);
    List<Kingdom> kingdoms = kingdomRepository.findKingdomsByPlayer(player);
    for (Kingdom kingdom : kingdoms) {
      PlayerKingdomResponseDTO dto = getPlayerKingdomResponseDTO(kingdom);
      dtoList.add(dto);
    }
    return dtoList;
  }

  @Override
  public PlayerKingdomResponseDTO getPlayerKingdomResponseDTO(Kingdom kingdom) {
    return new PlayerKingdomResponseDTO(
        kingdom.getId(),
        kingdom.getName(),
        kingdom.getWorld().getId(),
        kingdom.getWorld().getName(),
        kingdom.getCoordinateX(),
        kingdom.getCoordinateY(),
        kingdom.getResources().get(0).getAmount(),
        kingdom.getResources().get(1).getAmount(),
        kingdom.getBuildings().get(0).getLevel(),
        kingdom.getBuildings().get(1).getLevel(),
        kingdom.getBuildings().get(2).getLevel(),
        kingdom.getBuildings().get(3).getLevel());
  }

  @Override
  public KingdomResponseDTO returnKingdomDTOById(Long id) {
    Optional<Kingdom> kingdomOptional = kingdomRepository.findById(id);
    if (kingdomOptional.isPresent()) {
      return createKingdomDTO(kingdomOptional.get());
    } else {
      throw new CredentialException("Kingdom not found");
    }
  }

  @Override
  public boolean checkId(Long id) {
    return kingdomRepository.existsById(id);
  }

  @Override
  public void createStartingKingdom(Player player) {
    World databaseWorld = worldRepository.findWorldWithLessThan5Kingdoms();
    Kingdom kingdom;
    double[] coordinates = generateCoordinates();
    if (databaseWorld == null) {
      World world = new World();
      world.getPlayers().add(player);

      kingdom = new Kingdom(coordinates[0], coordinates[1], player, world);
      List<Kingdom> kingdoms = new ArrayList<>();
      kingdoms.add(kingdom);

      world.setKingdoms(kingdoms);

      worldRepository.save(world);
      kingdomRepository.save(kingdom);
    } else {
      kingdom = new Kingdom(coordinates[0], coordinates[1], player, databaseWorld);

      List<Kingdom> kingdoms = databaseWorld.getKingdoms();
      kingdoms.add(kingdom);
      databaseWorld.setKingdoms(kingdoms);
      databaseWorld.getPlayers().add(player);

      worldRepository.save(databaseWorld);
      kingdomRepository.save(kingdom);
    }
    for (BuildingTypeValue buildingType : BuildingTypeValue.values()) {
      Building building = new Building(kingdom, buildingType, true);
      buildingRepository.save(building);
    }
    for (ResourceTypeValue resourceType : ResourceTypeValue.values()) {
      Resource resource = new Resource(kingdom, resourceType, 100);
      resourceRepository.save(resource);
    }
  }

  @Override
  public double[] generateCoordinates() {
    Random random = new Random();
    double coordinateX = 0;
    double coordinateY = 0;
    boolean coordinatesExists = true;
    double[] coordinates = new double[2];
    while (coordinatesExists) {
      coordinateX = random.nextInt(100) + 1;
      coordinateY = random.nextInt(100) + 1;
      if (!kingdomRepository.existsKingdomByCoordinateXAndCoordinateY(coordinateX, coordinateY)) {
        coordinatesExists = false;
        coordinates[0] = coordinateX;
        coordinates[1] = coordinateY;
      }
    }
    return coordinates;
  }
}
