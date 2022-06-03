package grn.database.pojo;


import grn.database.QueryRow;
import grn.database.repository.Repositories;
import grn.database.repository.ChampionRepository;
import org.json.simple.JSONObject;

import java.sql.Timestamp;

public class ChampionMastery {

  private long playerId;
  private long championId;
  private long championLevel;
  private long championPoints;
  private java.sql.Timestamp lastPlayTime;
  private Champion champion;
  private String championIcon;
  private String championName;

  public ChampionMastery (JSONObject jPlayer, Player player) {
    fromJson(jPlayer);
    this.playerId = player.getInternalId();
  }

  public ChampionMastery (QueryRow row) {
    fromQueryRow(row);
  }

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

    ChampionRepository championRepository =
            Repositories.getDataDragonRepository().getChampionRepository();
    this.champion = championRepository.getChampion(championId);
    this.championIcon = champion.getIcon();
    this.championName = champion.getName();
  }

  public Champion getChampion() {
    return champion;
  }

  public String getChampionIcon() {
    return championIcon;
  }

  public long getPlayerId() {
    return playerId;
  }

  public long getChampionId() {
    return championId;
  }

  public long getChampionLevel() {
    return championLevel;
  }

  public long getChampionPoints() {
    return championPoints;
  }

  public java.sql.Timestamp getLastPlayTime() {
    return lastPlayTime;
  }

}
