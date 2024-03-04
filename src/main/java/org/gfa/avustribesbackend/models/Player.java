package org.gfa.avustribesbackend.models;

import jakarta.persistence.*;
import org.gfa.avustribesbackend.models.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "players")
public class Player implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id", unique = true, nullable = false)
  private Long id;

  @Column(name = "player_name", unique = true, nullable = false)
  private String playerName;
  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "verified_at")
  private Date verifiedAt;

  @Column(name = "is_verified")
  private Boolean isVerified;

  @Column(name = "created_at", nullable = false)
  private Date createdAt;

  @ManyToMany(mappedBy = "players")
  private List<World> worlds;

  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToOne(mappedBy = "player")
  private EmailVerification emailVerification;

  @OneToOne(mappedBy = "player")
  private PasswordReset passwordReset;

  public Player() {
    this.createdAt = new Date(System.currentTimeMillis());
    this.isVerified = false;
    this.role = Role.USER;
  }

  public Player(
      String playerName,
      String email,
      String password) {
    this.playerName = playerName;
    this.email = email;
    this.password = password;
    this.isVerified = false;
    this.createdAt = new Date(System.currentTimeMillis());
    this.role = Role.USER;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Date getVerifiedAt() {
    return verifiedAt;
  }

  public void setVerifiedAt(Date verifiedAt) {
    this.verifiedAt = verifiedAt;
  }

  public Boolean getIsVerified() {
    return isVerified;
  }

  public void setIsVerified(Boolean verified) {
    isVerified = verified;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public List<World> getWorlds() {
    return worlds;
  }

  public void setWorlds(List<World> worlds) {
    this.worlds = worlds;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public Boolean getVerified() {
    return isVerified;
  }

  public void setVerified(Boolean verified) {
    isVerified = verified;
  }

  public EmailVerification getEmailVerification() {
    return emailVerification;
  }

  public void setEmailVerification(EmailVerification emailVerification) {
    this.emailVerification = emailVerification;
  }

  public PasswordReset getPasswordReset() {
    return passwordReset;
  }

  public void setPasswordReset(PasswordReset passwordReset) {
    this.passwordReset = passwordReset;
  }
}
