package org.gfa.avustribesbackend.services.Email;

import org.gfa.avustribesbackend.models.EmailVerification;
import org.gfa.avustribesbackend.models.Player;
import org.gfa.avustribesbackend.repositories.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailVerificationTest {
  @Mock private PlayerRepository playerRepository;
  @Mock private EmailVerificationServiceImpl emailVerificationService;

  @Test
  void verifyEmail_withExpiredToken_shouldReturnFalse() {
    // Arrange
    MockitoAnnotations.openMocks(this);

    String token = "expiredToken";
    Player player = new Player();
    EmailVerification emailVerification = new EmailVerification(token, player);
    player.setEmailVerification(emailVerification);
    Date pastDate = new Date(System.currentTimeMillis() - 1000000);
    player.getEmailVerification().setExpiresAt(pastDate);

    when(playerRepository.findByEmailVerificationToken(token)).thenReturn(player);

    // Act
    boolean result = emailVerificationService.verifyEmail(token);

    // Assert
    assertFalse(result);
    assertNull(player.getVerifiedAt());
  }
}