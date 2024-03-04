package org.gfa.avustribesbackend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "troops")
public class Troop {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id", unique = true, nullable = false)
  private Long troopId;

  @Column(name = "level", nullable = false)
  private Integer level;

  @Column(name = "hp", nullable = false)
  private Integer hp;

  @Column(name = "attack", nullable = false)
  private Integer attack;

  @Column(name = "defense", nullable = false)
  private Integer defense;

  @Column(name = "started_at", nullable = false)
  private Long startedAt;

  @Column(name = "finished_at", nullable = false)
  private Long finishedAt;

  @ManyToOne
  @JoinColumn(name = "kingdom_id", nullable = false)
  private Kingdom kingdom;

  public Troop(Integer level, Kingdom kingdom, Long startedAt, Long finishedAt) {
    this.level = level;
    this.hp = 20;
    this.attack = 10;
    this.defense = 5;
    this.kingdom = kingdom;
    this.startedAt = startedAt;
    this.finishedAt = finishedAt;
  }

  public Troop() {}

  public Long getTroopId() {
    return troopId;
  }

  public void setTroopId(Long troopId) {
    this.troopId = troopId;
  }

  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    if (level > 0) {
      this.level = level;
    } else {
      throw new IllegalArgumentException("Level cannot be less than 1");
    }
  }

  public Integer getHp() {
    return hp;
  }

  public void setHp(Integer hp) {
    if (hp > 0) {
      this.hp = hp;
    } else {
      throw new IllegalArgumentException("Hp cannot be less than 1");
    }
  }

  public Integer getAttack() {
    return attack;
  }

  public void setAttack(Integer attack) {
    if (attack > 0) {
      this.attack = attack;
    } else {
      throw new IllegalArgumentException("Attack cannot be less than 1");
    }
  }

  public Integer getDefense() {
    return defense;
  }

  public void setDefense(Integer defense) {
    if (defense >= 0) {
      this.defense = defense;
    } else {
      throw new IllegalArgumentException("Defense cannot be less than 0");
    }
  }

  public Long getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(Long startedAt) {
    this.startedAt = startedAt;
  }

  public Long getFinishedAt() {
    return finishedAt;
  }

  public void setFinishedAt(Long finishedAt) {
    this.finishedAt = finishedAt;
  }

  public Kingdom getKingdom() {
    return kingdom;
  }

  public void setKingdom(Kingdom kingdom) {
    this.kingdom = kingdom;
  }
}
