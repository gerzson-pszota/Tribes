package org.gfa.avustribesbackend.dtos;

public class PasswordRequestDTO {

  private String password;

  public PasswordRequestDTO() {
  }

  public PasswordRequestDTO(String password) {
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
