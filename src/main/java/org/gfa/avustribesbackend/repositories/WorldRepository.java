package org.gfa.avustribesbackend.repositories;

import org.gfa.avustribesbackend.models.World;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorldRepository extends JpaRepository<World, Long> {
  @Query("SELECT world FROM World world WHERE SIZE(world.kingdoms) < 5")
  World findWorldWithLessThan5Kingdoms();
}
