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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.gfa.avustribesbackend.models.enums.BuildingTypeValue.*;

@Service
public class BuildingServiceImpl implements BuildingService {
  private final KingdomRepository kingdomRepository;
  private final BuildingRepository buildingRepository;
  private final ResourceRepository resourceRepository;
  public static final Map<BuildingTypeValue, Integer> buildingConstructionTimes = new HashMap<>();
  public static final Map<BuildingTypeValue, Integer> buildingCosts = new HashMap<>();

  static {
    buildingConstructionTimes.put(TOWNHALL, 5); // in minutes
    buildingConstructionTimes.put(FARM, 1);
    buildingConstructionTimes.put(MINE, 1);
    buildingConstructionTimes.put(ACADEMY, 3);

    buildingCosts.put(TOWNHALL, 200);
    buildingCosts.put(FARM, 100);
    buildingCosts.put(MINE, 100);
    buildingCosts.put(ACADEMY, 150);
  }

  @Autowired
  public BuildingServiceImpl(
      KingdomRepository kingdomRepository,
      BuildingRepository buildingRepository,
      ResourceRepository resourceRepository) {
    this.kingdomRepository = kingdomRepository;
    this.buildingRepository = buildingRepository;
    this.resourceRepository = resourceRepository;
  }

  @Override
  public void upgradeBuilding(Kingdom kingdom, BuildingTypeValue buildingType, Resource gold) {

    int currentLevel = buildingRepository.getBuildingLevel(kingdom, buildingType);
    int upgradeCost = calculateUpgradeCost(currentLevel, buildingType);
    int maxAllowedLevel = buildingRepository.getBuildingLevel(kingdom, BuildingTypeValue.TOWNHALL);
    Building building = buildingRepository.findByKingdomAndType(kingdom, buildingType);

    if (currentLevel >= 10) {
      throw new BuildingException("Building is already at max level.");
    }

    if (gold.getAmount() < upgradeCost) {
      throw new BuildingException("Not enough gold to upgrade the building.");
    }

    if (buildingType != BuildingTypeValue.TOWNHALL && currentLevel >= maxAllowedLevel) {
      throw new BuildingException("Cannot upgrade the building beyond the Townhall level.");
    }

    gold.setAmount(gold.getAmount() - upgradeCost);
    building.incrementLevel();

    buildingRepository.save(building);
    resourceRepository.save(gold);
  }

  @Override
  public boolean buildNewBuilding(BuildNewBuildingDTO dto) {
    if (!isBuildingPossible(dto)) {
      return false;
    }

    Optional<Kingdom> kingdomOptional = kingdomRepository.findById(dto.getKingdomId());
    Kingdom kingdom = kingdomOptional.orElseThrow(() -> new RuntimeException("Kingdom not found"));

    int goldCost = getBuildingCost(dto.getType());
    List<Resource> resources = kingdom.getResources();
    Resource goldResource =
        resources.stream()
            .filter(resource -> resource.getType() == ResourceTypeValue.GOLD)
            .findFirst()
            .orElseThrow(() -> new BuildingException("Gold resource not found in the kingdom."));

    goldResource.setAmount(goldResource.getAmount() - goldCost);
    Building building = new Building(kingdom, dto.getType());
    building.setConstructionStartTime(LocalDateTime.now());
    buildingRepository.save(building);
    resourceRepository.save(goldResource);
    return true;
  }

  @Scheduled(fixedRate = 60000)
  public void checkConstructionStatus() {
    List<Building> buildingsUnderConstruction =
        buildingRepository.findByConstructionStartTimeIsNotNull();
    for (Building building : buildingsUnderConstruction) {
      if (isConstructionComplete(building)) {
        building.setConstructionStartTime(null);
        building.setBuildingFinished(true);
        buildingRepository.save(building);
      }
    }
  }

  private int calculateUpgradeCost(int currentLevel, BuildingTypeValue buildingType) {
    int baseCost;
    if (buildingType.equals(TOWNHALL)) {
      baseCost = 200;
    } else {
      baseCost = 100;
    }
    return baseCost * currentLevel;
  }

  private boolean isConstructionComplete(Building building) {
    BuildingTypeValue type = building.getType();
    int constructionTime = buildingConstructionTimes.get(type);
    LocalDateTime startTime = building.getConstructionStartTime();
    if (startTime == null) {
      return false;
    }
    LocalDateTime currentTime = LocalDateTime.now();
    Duration duration = Duration.between(startTime, currentTime);
    return duration.compareTo(Duration.ofMinutes(constructionTime)) >= 0;
  }

  private boolean isBuildingPossible(BuildNewBuildingDTO dto) {
    Optional<Kingdom> kingdomOptional = kingdomRepository.findById(dto.getKingdomId());
    Kingdom kingdom = kingdomOptional.orElseThrow(() -> new RuntimeException("Kingdom not found"));

    if (kingdom.getResources() == null) {
      kingdom.setResources(new ArrayList<>());
    }

    int gold = getGoldAmount(kingdom);
    BuildingTypeValue type = dto.getType();
    int buildingCost = getBuildingCost(type);
    Building townhall = buildingRepository.findByKingdomAndType(kingdom, TOWNHALL);
    boolean hasTownhall = townhall != null;
    if (hasTownhall && type.equals(TOWNHALL)) {
      throw new BuildingException("You already have a townhall!");
    }
    if (!hasTownhall && !type.equals(TOWNHALL)) {
      throw new BuildingException("You need to build a townhall first!");
    }
    if (gold >= buildingCost) {
      return true;
    } else {
      throw new BuildingException("You don't have enough gold!");
    }
  }

  private int getBuildingCost(BuildingTypeValue type) {
    return buildingCosts.get(type);
  }

  private int getGoldAmount(Kingdom kingdom) {
    List<Resource> resources = kingdom.getResources();
    for (Resource resource : resources) {
      if (ResourceTypeValue.GOLD.equals(resource.getType())) {
        return resource.getAmount();
      }
    }
    return 0;
  }
}
