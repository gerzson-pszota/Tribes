package org.gfa.avustribesbackend.services.Troop;

import org.gfa.avustribesbackend.exceptions.CreationException;
import org.gfa.avustribesbackend.models.Building;
import org.gfa.avustribesbackend.models.Kingdom;
import org.gfa.avustribesbackend.models.Resource;
import org.gfa.avustribesbackend.models.Troop;
import org.gfa.avustribesbackend.models.enums.BuildingTypeValue;
import org.gfa.avustribesbackend.models.enums.ResourceTypeValue;
import org.gfa.avustribesbackend.repositories.BuildingRepository;
import org.gfa.avustribesbackend.repositories.ResourceRepository;
import org.gfa.avustribesbackend.repositories.TroopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TroopServiceImpl implements TroopService {
  private final TroopRepository troopRepository;
  private final BuildingRepository buildingRepository;
  private final ResourceRepository resourceRepository;

  @Autowired
  public TroopServiceImpl(
      TroopRepository troopRepository,
      BuildingRepository buildingRepository,
      ResourceRepository resourceRepository) {
    this.troopRepository = troopRepository;
    this.buildingRepository = buildingRepository;
    this.resourceRepository = resourceRepository;
  }

  @Override
  public void create(Kingdom kingdom) {
    this.checkTroopCreation(kingdom);
    // creating troop
    Resource gold = resourceRepository.findByKingdomAndType(kingdom, ResourceTypeValue.GOLD);
    gold.setAmount(gold.getAmount() - 25);
    resourceRepository.save(gold);
    Troop databaseTroop = troopRepository.findFirstByKingdomOrderByLevelDesc(kingdom);
    Troop troop;
    if (databaseTroop == null) {
      troop = new Troop(1, kingdom, System.currentTimeMillis(), System.currentTimeMillis() + 30);
    } else {
      troop =
          new Troop(
              databaseTroop.getLevel(),
              kingdom,
              System.currentTimeMillis(),
              System.currentTimeMillis() + 30);
    }
    // Pause for 30 seconds
    try {
      Thread.sleep(30000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    troopRepository.save(troop);
  }

  @Override
  @Scheduled(fixedRate = 60000)
  public void eat() {
    List<Troop> troops = troopRepository.findAll();
    for (Troop troop : troops) {
      Resource resource =
          resourceRepository.findByKingdomAndType(troop.getKingdom(), ResourceTypeValue.FOOD);
      resource.setAmount(resource.getAmount() - (troop.getLevel() * 5));
      resourceRepository.save(resource);
    }
  }

  @Override
  public void upgrade(Kingdom kingdom) {
    if (resourceRepository.findByKingdomAndType(kingdom, ResourceTypeValue.GOLD).getAmount()
        < 500) {
      throw new CreationException("Not enough gold to upgrade troops");
    }
    // pay 500 in gold
    Resource resource = resourceRepository.findByKingdomAndType(kingdom, ResourceTypeValue.GOLD);
    resource.setAmount(resource.getAmount() - 500);
    resourceRepository.save(resource);
    List<Troop> allTroops = troopRepository.findAllByKingdom(kingdom);
    // Pause for 60 seconds
    try {
      Thread.sleep(60000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    // upgrade
    for (Troop troop : allTroops) {
      troop.setAttack(troop.getAttack() + 10);
      troop.setDefense(troop.getDefense() + 5);
      troop.setHp(troop.getHp() + 10);
      troop.setLevel(troop.getLevel() + 1);
      troopRepository.save(troop);
    }
  }

  public void checkTroopCreation(Kingdom kingdom) {
    // checks if there is enough gold
    if (resourceRepository.getResourceAmount(kingdom, ResourceTypeValue.GOLD) < 25) {
      throw new CreationException("Not enough gold to create troop");
    }
    // checks academy
    boolean hasAcademy = false;
    if (kingdom.getBuildings() != null) {
      for (Building building : kingdom.getBuildings()) {
        if (building.getType().equals(BuildingTypeValue.ACADEMY)) {
          hasAcademy = true;
          break;
        }
      }
    }
    if (!hasAcademy) {
      throw new CreationException("Need academy to train troops!");
    }
    // checks if there is enough food production
    int totalFoodPerMinute = 0;
    for (Building building :
        buildingRepository.findAllByKingdomAndType(kingdom, BuildingTypeValue.FARM)) {
      totalFoodPerMinute += (building.getLevel() * 5) + 5;
    }
    int totalEating = 0;
    for (Troop troop : troopRepository.findAll()) {
      totalEating += troop.getLevel() * 5;
    }
    if (totalEating >= totalFoodPerMinute) {
      throw new CreationException("No enough food to feed another hungry troop");
    }
  }
}
