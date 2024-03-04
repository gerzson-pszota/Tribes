package org.gfa.avustribesbackend.models;

import jakarta.persistence.*;
import org.gfa.avustribesbackend.models.enums.ResourceTypeValue;

@Entity
@Table(name = "resources")
public class Resource {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "kingdom_id", nullable = false)
  private Kingdom kingdom;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private ResourceTypeValue type;

  private int amount;

  public Resource() {
    this.amount = 0;
  }

  public Resource(Kingdom kingdom, ResourceTypeValue type, int amount) {
    this.kingdom = kingdom;
    this.type = type;
    this.amount = amount;
  }

  public Resource(Kingdom kingdom, ResourceTypeValue type) {
    this.kingdom = kingdom;
    this.type = type;
    this.amount = 0;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Kingdom getKingdom() {
    return kingdom;
  }

  public void setKingdom(Kingdom kingdom) {
    this.kingdom = kingdom;
  }

  public ResourceTypeValue getType() {
    return type;
  }

  public void setType(ResourceTypeValue type) {
    this.type = type;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Amount cannot be negative");
    }
    this.amount = amount;
  }
}
