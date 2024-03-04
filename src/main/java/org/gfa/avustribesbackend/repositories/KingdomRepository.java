package org.gfa.avustribesbackend.repositories;

import org.gfa.avustribesbackend.models.Kingdom;
import org.gfa.avustribesbackend.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KingdomRepository extends JpaRepository<Kingdom, Long> {

  long countAllByWorld_Id(long id);

  List<Kingdom> findKingdomsByPlayer(Player player);

  boolean existsKingdomByCoordinateXAndCoordinateY(double x, double y);
}
