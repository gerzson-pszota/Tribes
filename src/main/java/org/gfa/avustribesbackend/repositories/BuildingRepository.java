package org.gfa.avustribesbackend.repositories;

import org.gfa.avustribesbackend.models.Building;
import org.gfa.avustribesbackend.models.Kingdom;
import org.gfa.avustribesbackend.models.enums.BuildingTypeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BuildingRepository extends JpaRepository<Building, Long> {
  List<Building> findAllByKingdomAndType(Kingdom kingdom, BuildingTypeValue buildingTypeValue);

  @Query("SELECT b FROM Building b WHERE b.constructionStartTime IS NOT NULL")
  List<Building> findByConstructionStartTimeIsNotNull();

  @Query("SELECT b.level FROM Building b WHERE b.kingdom = :kingdom AND b.type = :buildingType")
  int getBuildingLevel(Kingdom kingdom, BuildingTypeValue buildingType);

  Building findByKingdomAndType(Kingdom kingdom, BuildingTypeValue buildingTypeValue);
}
