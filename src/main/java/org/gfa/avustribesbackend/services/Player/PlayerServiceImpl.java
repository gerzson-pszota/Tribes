package org.gfa.avustribesbackend.services.Player;

import org.gfa.avustribesbackend.dtos.AuthenticationRequest;
import org.gfa.avustribesbackend.dtos.PlayerInfoDTO;
import org.gfa.avustribesbackend.dtos.PlayerRegistrationBody;
import org.gfa.avustribesbackend.dtos.TokenDTO;
import org.gfa.avustribesbackend.exceptions.*;
import org.gfa.avustribesbackend.models.EmailVerification;
import org.gfa.avustribesbackend.models.Player;
import org.gfa.avustribesbackend.repositories.EmailVerificationRepository;
import org.gfa.avustribesbackend.repositories.PlayerRepository;
import org.gfa.avustribesbackend.services.Email.EmailVerificationService;
import org.gfa.avustribesbackend.services.Kingdom.KingdomService;
import org.gfa.avustribesbackend.services.JWT.JwtServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import io.github.cdimascio.dotenv.Dotenv;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

@Service
public class PlayerServiceImpl implements PlayerService {
  Dotenv dotenv = Dotenv.configure().load();
  String verifyEmailEnabled = dotenv.get("VERIFY_EMAIL_ENABLED");

  private final PlayerRepository playerRepository;
  private final EmailVerificationService emailVerificationService;
  private final KingdomService kingdomService;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtServiceImpl jwtServiceImpl;
  private final EmailVerificationRepository emailVerificationRepository;

  @Autowired
  public PlayerServiceImpl(
      PlayerRepository playerRepository,
      EmailVerificationService emailVerificationService,
      PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager,
      JwtServiceImpl jwtServiceImpl,
      KingdomService kingdomService, EmailVerificationRepository emailVerificationRepository) {
    this.playerRepository = playerRepository;
    this.emailVerificationService = emailVerificationService;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtServiceImpl = jwtServiceImpl;
    this.kingdomService = kingdomService;
    this.emailVerificationRepository = emailVerificationRepository;
  }

  @Override
  public ResponseEntity<Object> registerPlayer(PlayerRegistrationBody request) {
    if (request.getUsername() == null || request.getUsername().isEmpty()) {
      throw new CredentialException("Username is required");
    } else if (request.getPassword() == null || request.getPassword().isEmpty()) {
      throw new CredentialException("Password is required");
    } else if (request.getEmail() == null || request.getEmail().isEmpty()) {
      throw new CredentialException("Email is required");
    } else if (playerRepository.existsByPlayerName(request.getUsername())) {
      throw new AlreadyExistsException("Username is already taken");
    } else if (playerRepository.existsByEmailIgnoreCase(request.getEmail())) {
      throw new AlreadyExistsException("Email is already taken");
    } else if (request.getUsername().length() < 4) {
      throw new CredentialException("Username must be at least 4 characters long");
    } else if (request.getPassword().length() < 8) {
      throw new CredentialException("Password must be at least 8 characters long");
    } else if (!validateEmail(request.getEmail())) {
      throw new CredentialException("Invalid email");
    }

    Player player =
        new Player(
            request.getUsername(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()));

    if (player == null) {
      throw new CreationException("Unknown error");
    }

    EmailVerification emailVerification =
        new EmailVerification(verificationToken(), player);

    player.setEmailVerification(emailVerification);

    emailVerificationRepository.save(emailVerification);
    playerRepository.save(player);

    if (verifyEmailEnabled.equals("true")) {
      String token = player.getEmailVerification().getToken();
      emailVerificationService.sendVerificationEmail(token);
    } else {
      Date date = new Date(System.currentTimeMillis());
      player.setVerifiedAt(date);
      player.setIsVerified(true);
      playerRepository.save(player);
      kingdomService.createStartingKingdom(player);
    }
    return ResponseEntity.ok("successful creation");
  }

  @Override
  public boolean validateEmail(String email) {
    boolean isValid = false;
    try {
      InternetAddress internetAddress = new InternetAddress(email);
      internetAddress.validate();
      isValid = true;
    } catch (AddressException e) {
      e.printStackTrace();
    }
    return isValid;
  }

  @Override
  public String verificationToken() {
    SecureRandom secureRandom = new SecureRandom();
    byte[] tokenBytes = new byte[32];
    secureRandom.nextBytes(tokenBytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
  }

  @Override
  public PlayerInfoDTO createPlayerInfoDTO(Player player) {
    PlayerInfoDTO dto = null;
    if (player.getIsVerified()) {
      dto =
          new PlayerInfoDTO(
              player.getId(),
              player.getPlayerName(),
              player.getIsVerified(),
              player.getVerifiedAt());
    } else {
      dto = new PlayerInfoDTO(player.getId(), player.getPlayerName(), player.getIsVerified(), null);
    }
    return dto;
  }

  @Override
  public List<PlayerInfoDTO> listPlayerInfoDTO() {
    List<Player> allPlayers = playerRepository.findAll();
    List<PlayerInfoDTO> dtoList = new ArrayList<>();
    for (Player player : allPlayers) {
      PlayerInfoDTO dto = createPlayerInfoDTO(player);
      dtoList.add(dto);
    }
    return dtoList;
  }

  @Override
  public PlayerInfoDTO findPlayerDTOById(Long id) {
    Optional<Player> playerOptional = playerRepository.findById(id);
    if (playerOptional.isPresent()) {
      PlayerInfoDTO dto = createPlayerInfoDTO(playerOptional.get());
      return dto;
    } else {
      throw new CredentialException("Player not found");
    }
  }

  @Override
  public boolean checkId(Long id) {
    return playerRepository.existsById(id);
  }

  @Override
  public ResponseEntity<Object> login(AuthenticationRequest request) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    } catch (AuthenticationException e) {
      throw new CredentialException("Invalid credentials");
    }
    Player player = playerRepository.findByEmailIgnoreCase(request.getEmail());
    if (player == null) {
      throw new NotFoundException("Player not found.");
    }
    TokenDTO token = new TokenDTO(jwtServiceImpl.generateToken(player));
    return ResponseEntity.ok().body(token);
  }
}
