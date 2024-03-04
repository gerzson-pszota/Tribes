package org.gfa.avustribesbackend.dtos;

public class PlayerKingdomResponseDTO {

  private Long kingdomId;
  private String kingdomName;
  private Long worldId;
  private String worldName;
  private Double coordinateX;
  private Double coordinateY;
  private Integer goldAmount;
  private Integer foodAmount;
  private Integer townHallLevel;
  private Integer farmLevel;
  private Integer mineLevel;
  private Integer academyLevel;

  public PlayerKingdomResponseDTO() {
  }

  public PlayerKingdomResponseDTO(
      Long kingdomId,
      String kingdomName,
      Long worldId,
      String worldName,
      Double coordinateX,
      Double coordinateY,
      Integer goldAmount,
      Integer foodAmount,
      Integer townHallLevel,
      Integer farmLevel,
      Integer mineLevel,
      Integer academyLevel) {
    this.kingdomId = kingdomId;
    this.kingdomName = kingdomName;
    this.worldId = worldId;
    this.worldName = worldName;
    this.coordinateX = coordinateX;
    this.coordinateY = coordinateY;
    this.goldAmount = goldAmount;
    this.foodAmount = foodAmount;
    this.townHallLevel = townHallLevel;
    this.farmLevel = farmLevel;
    this.mineLevel = mineLevel;
    this.academyLevel = academyLevel;
  }

  public Long getKingdomId() {
    return kingdomId;
  }

  public void setKingdomId(Long kingdomId) {
    this.kingdomId = kingdomId;
  }

  public String getKingdomName() {
    return kingdomName;
  }

  public void setKingdomName(String kingdomName) {
    this.kingdomName = kingdomName;
  }

  public Long getWorldId() {
    return worldId;
  }

  public void setWorldId(Long worldId) {
    this.worldId = worldId;
  }

  public String getWorldName() {
    return worldName;
  }

  public void setWorldName(String worldName) {
    this.worldName = worldName;
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

  public Integer getGoldAmount() {
    return goldAmount;
  }

  public void setGoldAmount(Integer goldAmount) {
    this.goldAmount = goldAmount;
  }

  public Integer getFoodAmount() {
    return foodAmount;
  }

  public void setFoodAmount(Integer foodAmount) {
    this.foodAmount = foodAmount;
  }

  public Integer getTownHallLevel() {
    return townHallLevel;
  }

  public void setTownHallLevel(Integer townHallLevel) {
    this.townHallLevel = townHallLevel;
  }

  public Integer getFarmLevel() {
    return farmLevel;
  }

  public void setFarmLevel(Integer farmLevel) {
    this.farmLevel = farmLevel;
  }

  public Integer getMineLevel() {
    return mineLevel;
  }

  public void setMineLevel(Integer mineLevel) {
    this.mineLevel = mineLevel;
  }

  public Integer getAcademyLevel() {
    return academyLevel;
  }

  public void setAcademyLevel(Integer academyLevel) {
    this.academyLevel = academyLevel;
  }
}
