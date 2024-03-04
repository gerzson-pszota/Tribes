package org.gfa.avustribesbackend.services.Resource;

import org.gfa.avustribesbackend.models.Building;
import org.gfa.avustribesbackend.models.Resource;
import org.gfa.avustribesbackend.models.enums.BuildingTypeValue;
import org.gfa.avustribesbackend.models.enums.ResourceTypeValue;
import org.gfa.avustribesbackend.repositories.BuildingRepository;
import org.gfa.avustribesbackend.repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {
  private final ResourceRepository resourceRepository;
  private final BuildingRepository buildingRepository;

  @Autowired
  public ResourceServiceImpl(
      ResourceRepository resourceRepository, BuildingRepository buildingRepository) {
    this.resourceRepository = resourceRepository;
    this.buildingRepository = buildingRepository;
  }

  @Override
  @Scheduled(fixedRate = 60000)
  public void gainingResources() {
    List<Resource> resources = resourceRepository.findAll();
    for (Resource resource : resources) {
      if (resource.getType() == ResourceTypeValue.GOLD) {
        for (Building building :
            buildingRepository.findAllByKingdomAndType(
                resource.getKingdom(), BuildingTypeValue.MINE)) {
          if (building.getLevel() == 1) {
            resource.setAmount(resource.getAmount() + 10);
          } else {
            resource.setAmount(resource.getAmount() + (10 + (5 * building.getLevel())));
          }
        }
      } else {
        for (Building building :
            buildingRepository.findAllByKingdomAndType(
                resource.getKingdom(), BuildingTypeValue.FARM)) {
          if (building.getLevel() == 1) {
            resource.setAmount(resource.getAmount() + 10);
          } else {
            resource.setAmount(resource.getAmount() + (10 + (5 * building.getLevel())));
          }
        }
      }
      resourceRepository.save(resource);
    }
  }
}
