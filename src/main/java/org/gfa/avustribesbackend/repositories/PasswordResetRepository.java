package org.gfa.avustribesbackend.repositories;

import org.gfa.avustribesbackend.models.PasswordReset;
import org.gfa.avustribesbackend.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
  Optional<PasswordReset> findFirstByPlayer(Player player);
}
