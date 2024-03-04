package org.gfa.avustribesbackend.controllers;

import org.gfa.avustribesbackend.dtos.*;
import org.gfa.avustribesbackend.exceptions.CredentialException;
import org.gfa.avustribesbackend.exceptions.EmailException;
import org.gfa.avustribesbackend.exceptions.VerificationException;
import org.gfa.avustribesbackend.services.Email.EmailVerificationService;
import org.gfa.avustribesbackend.services.ResetPassword.ResetPasswordService;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.gfa.avustribesbackend.services.Player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class PlayerController {

  private final EmailVerificationService emailVerificationService;
  private final PlayerService playerService;
  private final ResetPasswordService resetPasswordService;

  @Autowired
  public PlayerController(
      PlayerService playerService,
      EmailVerificationService emailVerificationService,
      ResetPasswordService resetPasswordService) {
    this.playerService = playerService;
    this.emailVerificationService = emailVerificationService;
    this.resetPasswordService = resetPasswordService;
  }

  @GetMapping("/email/verify/{token}")
  public Object verifyEmail(@PathVariable String token) {
    if (!emailVerificationService.isVerified(token)) {
      if (emailVerificationService.verifyEmail(token)) {
        return ResponseEntity.ok().body("User successfully verified");
      } else {
        return new RedirectView("/email/verify/resend", true);
      }
    } else {
      throw new EmailException("User already verified");
    }
  }

  @PostMapping("/register")
  public ResponseEntity<Object> register(@RequestBody PlayerRegistrationBody request) {
    return playerService.registerPlayer(request);
  }

  @PostMapping("/reset-password")
  public ResponseEntity<Object> sendResetPasswordEmail(@RequestBody EmailDTO email) {
    return resetPasswordService.sendResetPasswordEmail(email);
  }

  @PostMapping("/reset-password/{token}")
  public ResponseEntity<Object> resetPassword(
      @PathVariable TokenDTO token, @RequestBody PasswordRequestDTO password) {
    return resetPasswordService.resetPassword(token, password);
  }

  @Transactional(noRollbackFor = VerificationException.class)
  @PostMapping("/email/verify/resend")
  public ResponseEntity<Object> resendVerificationEmail(@RequestBody EmailDTO emailDTO) {
    emailVerificationService.resendVerificationEmail(emailDTO.getEmail());
    return ResponseEntity.ok().body("ok");
  }

  @GetMapping("/players")
  public ResponseEntity<Object> index() {
    return new ResponseEntity<>(playerService.listPlayerInfoDTO(), HttpStatus.OK);
  }

  @GetMapping("/players/{id}")
  public ResponseEntity<Object> index(@PathVariable Long id) {
    if (playerService.checkId(id)) {
      return new ResponseEntity<>(playerService.findPlayerDTOById(id), HttpStatus.OK);
    } else {
      throw new CredentialException("Player not found");
    }
  }

  @PostMapping("/login")
  public ResponseEntity<Object> login(@RequestBody AuthenticationRequest request) {
    return playerService.login(request);
  }
}
