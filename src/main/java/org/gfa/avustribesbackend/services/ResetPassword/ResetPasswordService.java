package org.gfa.avustribesbackend.services.ResetPassword;

import org.gfa.avustribesbackend.dtos.EmailDTO;
import org.gfa.avustribesbackend.dtos.PasswordRequestDTO;
import org.gfa.avustribesbackend.dtos.TokenDTO;
import org.springframework.http.ResponseEntity;

public interface ResetPasswordService {

  ResponseEntity<Object> sendResetPasswordEmail(EmailDTO email);

  ResponseEntity<Object> resetPassword(TokenDTO token, PasswordRequestDTO newPassword);
}
