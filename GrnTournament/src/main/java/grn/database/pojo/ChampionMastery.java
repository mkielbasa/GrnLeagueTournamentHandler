package grn.database.pojo;


public class ChampionMastery {

  private long playerId;
  private long championId;
  private long championLevel;
  private long championPoints;
  private java.sql.Timestamp lastPlayTime;


  public long getPlayerId() {
    return playerId;
  }

  public void setPlayerId(long playerId) {
    this.playerId = playerId;
  }


  public long getChampionId() {
    return championId;
  }

  public void setChampionId(long championId) {
    this.championId = championId;
  }


  public long getChampionLevel() {
    return championLevel;
  }

  public void setChampionLevel(long championLevel) {
    this.championLevel = championLevel;
  }


  public long getChampionPoints() {
    return championPoints;
  }

  public void setChampionPoints(long championPoints) {
    this.championPoints = championPoints;
  }


  public java.sql.Timestamp getLastPlayTime() {
    return lastPlayTime;
  }

  public void setLastPlayTime(java.sql.Timestamp lastPlayTime) {
    this.lastPlayTime = lastPlayTime;
  }

}
