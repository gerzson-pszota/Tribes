package org.gfa.avustribesbackend.models;

import jakarta.persistence.*;
import org.gfa.avustribesbackend.models.enums.BuildingTypeValue;

import java.time.LocalDateTime;

@Entity
@Table(name = "buildings")
public class Building {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private BuildingTypeValue type;

  @ManyToOne
  @JoinColumn(name = "kingdom_id", nullable = false)
  private Kingdom kingdom;

  private Integer level;

  @Column(name = "construction_start_time")
  private LocalDateTime constructionStartTime;

  @Column(name = "building_finished")
  private Boolean buildingFinished;

  public Building(Kingdom kingdom, BuildingTypeValue type) {
    this.kingdom = kingdom;
    this.type = type;
    this.level = 1;
    this.buildingFinished = false;
  }

  public Building(Kingdom kingdom, BuildingTypeValue type, Boolean buildingFinished) {
    this.kingdom = kingdom;
    this.type = type;
    this.level = 1;
    this.buildingFinished = buildingFinished;
  }

  public Building() {
    this.level = 1;
    this.buildingFinished = false;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BuildingTypeValue getType() {
    return type;
  }

  public void setType(BuildingTypeValue type) {
    this.type = type;
  }

  public Kingdom getKingdom() {
    return kingdom;
  }

  public void setKingdom(Kingdom kingdom) {
    this.kingdom = kingdom;
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

  public void incrementLevel() {
    this.level++;
  }

  public LocalDateTime getConstructionStartTime() {
    return constructionStartTime;
  }

  public void setConstructionStartTime(LocalDateTime constructionStartTime) {
    this.constructionStartTime = constructionStartTime;
  }

  public Boolean getBuildingFinished() {
    return buildingFinished;
  }

  public void setBuildingFinished(Boolean buildingFinished) {
    this.buildingFinished = buildingFinished;
  }
}
