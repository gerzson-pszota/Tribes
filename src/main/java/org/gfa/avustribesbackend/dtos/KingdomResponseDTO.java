package org.gfa.avustribesbackend.dtos;

public class KingdomResponseDTO {
  private final Long id;
  private final Long world;
  private final Long owner;
  private final String name;
  private final Double coordinateX;
  private final Double coordinateY;

  public KingdomResponseDTO(
      Long id, Long world, Long owner, String name, Double coordinateX, Double coordinateY) {
    this.id = id;
    this.world = world;
    this.owner = owner;
    this.name = name;
    this.coordinateX = coordinateX;
    this.coordinateY = coordinateY;
  }

  public Long getId() {
    return id;
  }

  public Long getWorld() {
    return world;
  }

  public Long getOwner() {
    return owner;
  }

  public String getName() {
    return name;
  }
}
