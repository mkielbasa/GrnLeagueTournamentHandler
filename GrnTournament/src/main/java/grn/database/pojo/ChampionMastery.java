package grn.database.pojo;


import grn.database.QueryRow;
import org.json.simple.JSONObject;

import java.sql.Timestamp;

public class ChampionMastery {

  private long playerId;
  private long championId;
  private long championLevel;
  private long championPoints;
  private java.sql.Timestamp lastPlayTime;

  public void fromJson (JSONObject jPlayer) {
      this.championId = (long)  jPlayer.get("championId");
      this.championLevel = (long) jPlayer.get("championLevel");
      this.championPoints = (long) jPlayer.get("championPoints");
      this.lastPlayTime = new Timestamp((long) jPlayer.get("lastPlayTime"));
  }

  public void fromQueryRow (QueryRow row) {
    this.playerId = (long) row.get(1);
    this.championId = (long) row.get(2);
    this.championLevel = (long) row.get(3);
    this.championPoints = (long) row.get(4);
    this.lastPlayTime = (Timestamp) row.get(5);
  }

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
