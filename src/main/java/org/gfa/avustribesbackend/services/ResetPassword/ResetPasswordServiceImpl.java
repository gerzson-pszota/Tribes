package org.gfa.avustribesbackend.services.ResetPassword;

import io.github.cdimascio.dotenv.Dotenv;
import org.gfa.avustribesbackend.dtos.EmailDTO;
import org.gfa.avustribesbackend.dtos.PasswordRequestDTO;
import org.gfa.avustribesbackend.dtos.TokenDTO;
import org.gfa.avustribesbackend.exceptions.CredentialException;
import org.gfa.avustribesbackend.exceptions.EmailException;
import org.gfa.avustribesbackend.exceptions.VerificationException;
import org.gfa.avustribesbackend.models.PasswordReset;
import org.gfa.avustribesbackend.models.Player;
import org.gfa.avustribesbackend.repositories.PasswordResetRepository;
import org.gfa.avustribesbackend.repositories.PlayerRepository;
import org.gfa.avustribesbackend.services.Player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Optional;

@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

  private final JavaMailSender javaMailSender;
  private final PlayerRepository playerRepository;
  private final PlayerService playerService;
  private final PasswordEncoder passwordEncoder;
  private final PasswordResetRepository passwordResetRepository;
  private final Dotenv dotenv = Dotenv.configure().load();
  private final String sender = dotenv.get("VERIFICATION_EMAIL_SENDER");
  private final String subject = dotenv.get("RESET_PASSWORD_EMAIL_SUBJECT");
  private final String resetPasswordUrl = dotenv.get("RESET_PASSWORD_URL");

  @Autowired
  public ResetPasswordServiceImpl(
      JavaMailSender javaMailSender,
      PlayerRepository playerRepository,
      PlayerService playerService, PasswordEncoder passwordEncoder, PasswordResetRepository passwordResetRepository) {
    this.javaMailSender = javaMailSender;
    this.playerRepository = playerRepository;
    this.playerService = playerService;
    this.passwordEncoder = passwordEncoder;
    this.passwordResetRepository = passwordResetRepository;
  }

  @Override
  public ResponseEntity<Object> sendResetPasswordEmail(EmailDTO email) {
    if (email == null || email.getEmail() == null || email.getEmail().isEmpty() || !playerRepository.existsByEmailIgnoreCase(email.getEmail())) {
      throw new CredentialException("Invalid email!");
    }

    Player player = playerRepository.findByEmailIgnoreCase(email.getEmail());

    if (!player.getIsVerified()) {
      throw new VerificationException("Unverified email!");
    }

    Optional<PasswordReset> optionalPasswordReset = passwordResetRepository.findFirstByPlayer(player);

    if (optionalPasswordReset.isEmpty()) {
      PasswordReset newPasswordReset = new PasswordReset(playerService.verificationToken(), player);
      passwordResetRepository.save(newPasswordReset);
      player.setPasswordReset(newPasswordReset);
      playerRepository.save(player);
    } else {
      PasswordReset passwordReset = optionalPasswordReset.get();
      passwordReset.setToken(playerService.verificationToken());
      passwordReset.setCreatedAt(new Date(System.currentTimeMillis()));
      passwordReset.setExpiresAt(new Date(System.currentTimeMillis() + 3600000));
      passwordResetRepository.save(passwordReset);
    }

    String htmlMessage = "<p>Hello " + player.getPlayerName() + ". If you want to reset your password please click <a href=\"" + resetPasswordUrl + player.getPasswordReset().getToken() + "\">" + resetPasswordUrl + player.getPasswordReset().getToken() + "</a></p>";

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
      helper.setTo(player.getEmail());
      helper.setFrom(sender);
      helper.setSubject(subject);
      helper.setText(htmlMessage, true);

      javaMailSender.send(mimeMessage);
    } catch (MessagingException e) {
      throw new EmailException("Unable to send email, please try again");
    }

    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<Object> resetPassword(TokenDTO token, PasswordRequestDTO newPassword) {
    if (newPassword == null || newPassword.getPassword() == null || newPassword.getPassword().isEmpty()) {
      throw new CredentialException("Password is required");
    }
    if (token == null || token.getToken() == null || token.getToken().isEmpty() || !playerRepository.existsByPasswordResetToken(token.getToken())) {
      throw new VerificationException("Invalid token");
    }
    if (newPassword.getPassword().length() < 8) {
      throw new CredentialException("Password must be at least 8 characters long");
    }

    Player player = playerRepository.findByPasswordResetToken(token.getToken());

    if (player.getPasswordReset().getExpiresAt().before(new Date(System.currentTimeMillis()))) {
      throw new VerificationException("Expired token");
    }

    player.setPassword(passwordEncoder.encode(newPassword.getPassword()));
    playerRepository.save(player);

    return ResponseEntity.ok().build();
  }
}

