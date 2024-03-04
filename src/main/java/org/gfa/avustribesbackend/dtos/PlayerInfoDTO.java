package org.gfa.avustribesbackend.dtos;

import java.util.Date;

public class PlayerInfoDTO {
  private Long id;
  private String username;
  private boolean isVerified;
  private Date verifiedAt;

  public PlayerInfoDTO(Long id, String username, boolean isVerified, Date verifiedAt) {
    this.id = id;
    this.username = username;
    this.isVerified = isVerified;
    this.verifiedAt = verifiedAt;
  }

  public PlayerInfoDTO() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public boolean getIsVerified() {
    return isVerified;
  }

  public void setIsVerified(boolean verified) {
    isVerified = verified;
  }

  public Date getVerifiedAt() {
    return verifiedAt;
  }

  public void setVerifiedAt(Date verifiedAt) {
    this.verifiedAt = verifiedAt;
  }
}
