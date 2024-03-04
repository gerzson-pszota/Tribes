package org.gfa.avustribesbackend.dtos;

import org.gfa.avustribesbackend.models.Player;
import org.gfa.avustribesbackend.repositories.PlayerRepository;
import org.gfa.avustribesbackend.services.Player.PlayerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PlayerInfoDTOTest {
  @InjectMocks private PlayerServiceImpl playerService;

  @Mock private PlayerRepository playerRepository;

  @Test
  void create_PlayerInfoDTO_verified() {
    // Arrange
    Player player = new Player("username", "email@test.com", "password");
    player.setIsVerified(true);
    player.setVerifiedAt(new Date(System.currentTimeMillis()));

    // Act
    PlayerInfoDTO dto = playerService.createPlayerInfoDTO(player);

    // Assert
    assertEquals(player.getPlayerName(), dto.getUsername());
    assertEquals(player.getIsVerified(), dto.getIsVerified());
    assertEquals(player.getVerifiedAt(), dto.getVerifiedAt());
  }

  @Test
  void create_PlayerInfoDTO_not_verified() {
    // Arrange
    Player player = new Player("username", "email@test.com", "password");
    player.setIsVerified(false);

    // Act
    PlayerInfoDTO dto = playerService.createPlayerInfoDTO(player);

    // Assert
    assertEquals(player.getPlayerName(), dto.getUsername());
    assertEquals(player.getIsVerified(), dto.getIsVerified());
    assertEquals(player.getVerifiedAt(), dto.getVerifiedAt());
  }
}
