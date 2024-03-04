package org.gfa.avustribesbackend.services.Email;

import io.github.cdimascio.dotenv.Dotenv;
import org.gfa.avustribesbackend.exceptions.CredentialException;
import org.gfa.avustribesbackend.exceptions.EmailException;
import org.gfa.avustribesbackend.exceptions.VerificationException;
import org.gfa.avustribesbackend.models.Player;
import org.gfa.avustribesbackend.repositories.PlayerRepository;
import org.gfa.avustribesbackend.services.Kingdom.KingdomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {
  private final JavaMailSender javaMailSender;
  private final PlayerRepository playerRepository;
  private final KingdomService kingdomService;
  private final Dotenv dotenv = Dotenv.configure().load();
  private final String url = dotenv.get("VERIFICATION_EMAIL_URL");
  private final String sender = dotenv.get("VERIFICATION_EMAIL_SENDER");
  private final String subject = dotenv.get("VERIFICATION_EMAIL_SUBJECT");
  private final String resendVerificationEmailSubject =
      dotenv.get("RESEND_VERIFICATION_EMAIL_SUBJECT");
  private final String templatePath = dotenv.get("VERIFICATION_EMAIL_TEMPLATE_FILEPATH");

  @Autowired
  public EmailVerificationServiceImpl(
      JavaMailSender javaMailSender,
      PlayerRepository playerRepository,
      KingdomService kingdomService) {
    this.javaMailSender = javaMailSender;
    this.playerRepository = playerRepository;
    this.kingdomService = kingdomService;
  }

  @Override
  public void sendVerificationEmail(String token) {
    Player player = playerRepository.findByEmailVerificationToken(token);

    if (player != null) {
      String user = player.getPlayerName();
      String verificationLink = url + token;

      String htmlTemplate = readHtmlTemplateFromFile(templatePath);
      htmlTemplate = htmlTemplate.replace("${user}", user);
      htmlTemplate = htmlTemplate.replace("${verificationLink}", verificationLink);

      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      try {
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(player.getEmail());
        helper.setFrom(sender);
        helper.setSubject(subject);
        helper.setText(htmlTemplate, true);

        javaMailSender.send(mimeMessage);
      } catch (MessagingException e) {
        throw new EmailException("Unable to send email, please try again");
      }
    }
  }

  @Override
  public boolean isVerified(Player player) {
    return player.getIsVerified();
  }

  @Override
  public boolean isVerified(String token) {
    Player player = playerRepository.findByEmailVerificationToken(token);
    if (player == null) {
      throw new EmailException("Player not found");
    }
    return player.getIsVerified();

  }

  @Override
  public boolean verifyEmail(String token) {
    Player player = playerRepository.findByEmailVerificationToken(token);

    if (player == null) {
      return false;
    }

    if (!isVerified(player)) {

      // check if token valid
      boolean tokenValid = false;
      Date expirationDate = player.getEmailVerification().getExpiresAt();
      Date currentDate = new Date(System.currentTimeMillis());
      if (currentDate.before(expirationDate)) {
        tokenValid = true;
      }

      // verify player
      if (tokenValid) {
        player.setVerifiedAt(currentDate);
        player.setIsVerified(true);
        playerRepository.save(player);
        kingdomService.createStartingKingdom(player);
        return true;
      } else {
        return false;
      }
    } else {
      throw new VerificationException("User already verified");
    }
  }

  @Override
  public void resendVerificationEmail(String email) {
    Player player = playerRepository.findByEmailIgnoreCase(email);

    if (player == null) {
      throw new CredentialException("Email address not found!");
    }

    if (player.getIsVerified()) {
      throw new VerificationException("Email already verified!");
    }

    String newToken = verificationToken();
    player.getEmailVerification().setToken(newToken);
    player.getEmailVerification().setCreatedAt(new Date(System.currentTimeMillis()));
    player.getEmailVerification().setExpiresAt(calculateTokenExpiration()); //tu zmenit

    String verificationLink = url + newToken;
    String htmlMessage =
        "<p>Please click <a href=\"" + verificationLink + "\">here</a> to verify your email.</p>";

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper helper;
    try {
      helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
      helper.setTo(player.getEmail());
      helper.setFrom(sender);
      helper.setSubject(resendVerificationEmailSubject);
      helper.setText(htmlMessage, true);

      javaMailSender.send(mimeMessage);
    } catch (MessagingException e) {
      throw new EmailException("Unable to send email, please try again");
    }
  }

  @Override
  public String verificationToken() {
    SecureRandom secureRandom = new SecureRandom();
    byte[] tokenBytes = new byte[32];
    secureRandom.nextBytes(tokenBytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
  }

  private String readHtmlTemplateFromFile(String filePath) {
    try {
      Path path = Paths.get(filePath);
      byte[] encoded = Files.readAllBytes(path);
      return new String(encoded, "UTF-8");
    } catch (IOException e) {
      throw new EmailException("Email template path not found");
    }
  }

  private Date calculateTokenExpiration() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.HOUR, 1);
    return calendar.getTime();
  }
}
