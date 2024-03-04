package org.gfa.avustribesbackend.dtos;

import org.gfa.avustribesbackend.models.enums.BuildingTypeValue;

public class BuildNewBuildingDTO {
  private final Long kingdomId;
  private final BuildingTypeValue type;

  public BuildNewBuildingDTO(Long kingdomId, BuildingTypeValue type) {
    this.kingdomId = kingdomId;
    this.type = type;
  }

  public Long getKingdomId() {
    return kingdomId;
  }

  public BuildingTypeValue getType() {
    return type;
  }
}
