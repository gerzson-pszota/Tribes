package org.gfa.avustribesbackend.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WorldResponseDto {
  private long id;
  private String name;
  private long kingdomCount;

  @JsonCreator
  public WorldResponseDto(
      @JsonProperty("id") long id,
      @JsonProperty("name") String name,
      @JsonProperty("kingdomCount") long kingdomCount) {
    this.id = id;
    this.name = name;
    this.kingdomCount = kingdomCount;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getKingdomCount() {
    return kingdomCount;
  }

  public void setKingdomCount(long kingdomCount) {
    this.kingdomCount = kingdomCount;
  }
}
