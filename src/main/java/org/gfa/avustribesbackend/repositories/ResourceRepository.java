package org.gfa.avustribesbackend.repositories;

import org.gfa.avustribesbackend.models.Kingdom;
import org.gfa.avustribesbackend.models.Resource;
import org.gfa.avustribesbackend.models.enums.ResourceTypeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
  @Query(
      "SELECT r.amount FROM Resource r WHERE r.kingdom = :kingdom AND r.type = :resourceTypeValue")
  int getResourceAmount(Kingdom kingdom, ResourceTypeValue resourceTypeValue);

  Resource findByKingdomAndType(Kingdom kingdom, ResourceTypeValue resourceTypeValue);
}
