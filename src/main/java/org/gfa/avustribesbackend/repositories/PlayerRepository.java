package org.gfa.avustribesbackend.repositories;

import org.gfa.avustribesbackend.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

  Player findByEmailVerificationToken(String token);

  boolean existsByPlayerName(String name);

  boolean existsByEmailIgnoreCase(String email);

  Player findByEmailIgnoreCase(String email);

  Player findByPasswordResetToken(String token);

  boolean existsByPasswordResetToken(String token);
}
