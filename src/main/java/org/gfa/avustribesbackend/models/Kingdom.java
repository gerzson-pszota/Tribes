package org.gfa.avustribesbackend.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "kingdoms")
public class Kingdom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "coordinate_x")
  private Double coordinateX;

  @Column(name = "coordinate_y")
  private Double coordinateY;

  @ManyToOne
  @JoinColumn(name = "player_id", nullable = false)
  private Player player;

  @ManyToOne
  @JoinColumn(name = "world_id", nullable = false)
  private World world;

  @OneToMany(mappedBy = "kingdom")
  private List<Resource> resources;

  @OneToMany(mappedBy = "kingdom")
  private List<Building> buildings;

  public Kingdom() {}

  public Kingdom(String name, Double coordinateX, Double coordinateY, Player player, World world) {
    this.name = name;
    this.coordinateX = coordinateX;
    this.coordinateY = coordinateY;
    this.player = player;
    this.world = world;
  }

  public Kingdom(Double coordinateX, Double coordinateY, Player player, World world) {
    this.name = player.getPlayerName() + "'s kingdom";
    this.coordinateX = coordinateX;
    this.coordinateY = coordinateY;
    this.player = player;
    this.world = world;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getCoordinateX() {
    return coordinateX;
  }

  public void setCoordinateX(Double coordinateX) {
    this.coordinateX = coordinateX;
  }

  public Double getCoordinateY() {
    return coordinateY;
  }

  public void setCoordinateY(Double coordinateY) {
    this.coordinateY = coordinateY;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public World getWorld() {
    return world;
  }

  public void setWorld(World world) {
    this.world = world;
  }

  public List<Resource> getResources() {
    return resources;
  }

  public void setResources(List<Resource> resources) {
    this.resources = resources;
  }

  public List<Building> getBuildings() {
    return buildings;
  }

  public void setBuildings(List<Building> buildings) {
    this.buildings = buildings;
  }
}
