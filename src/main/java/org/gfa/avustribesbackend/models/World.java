package org.gfa.avustribesbackend.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "worlds")
public class World {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(unique = true, nullable = false)
  private String name;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "world_player",
      joinColumns = @JoinColumn(name = "world_id"),
      inverseJoinColumns = @JoinColumn(name = "player_id"))
  private List<Player> players;

  @OneToMany(mappedBy = "world")
  private List<Kingdom> kingdoms;

  public World(String name) {
    this.name = name;
    this.players = new ArrayList<>();
    this.kingdoms = new ArrayList<>();
  }

  public World() {
    this.name = generatorOfName();
    this.players = new ArrayList<>();
    this.kingdoms = new ArrayList<>();
  }

  public String generatorOfName() {
    Random random = new Random();

    String[] StartOfName = {
      "Al", "Gr", "Han", "Poer", "Greg", "Ion", "Qer", "Sam", "Nup", "Res", "Clar", "Sour", "Wer",
      "Has", "Kil", "Dus", "Fux", "Jadus", "Taur"
    };

    String[] EndOfName = {
      "dor", "ion", "ian", "as", "or", "nor", "er", "ef", "fu", "gu", "vunu", "op", "nin"
    };

    return StartOfName[random.nextInt(StartOfName.length)]
        + EndOfName[random.nextInt(EndOfName.length)];
  }

  public List<Kingdom> getKingdoms() {
    return kingdoms;
  }

  public void setKingdoms(List<Kingdom> kingdoms) {
    this.kingdoms = kingdoms;
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

  public List<Player> getPlayers() {
    return players;
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }
}
