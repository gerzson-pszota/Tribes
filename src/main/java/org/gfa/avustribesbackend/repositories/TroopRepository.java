package org.gfa.avustribesbackend.repositories;

import org.gfa.avustribesbackend.models.Kingdom;
import org.gfa.avustribesbackend.models.Troop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TroopRepository extends JpaRepository<Troop, Long> {
  Troop findFirstByKingdomOrderByLevelDesc(Kingdom kingdom);

  List<Troop> findAllByKingdom(Kingdom kingdom);
}
