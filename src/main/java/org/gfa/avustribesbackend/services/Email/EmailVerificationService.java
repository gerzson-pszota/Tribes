package org.gfa.avustribesbackend.services.Email;

import org.gfa.avustribesbackend.models.Player;

public interface EmailVerificationService {

  boolean verifyEmail(String token);

  boolean isVerified(String token);

  boolean isVerified(Player player);

  void sendVerificationEmail(String email);

  void resendVerificationEmail(String email);

  String verificationToken();
}
