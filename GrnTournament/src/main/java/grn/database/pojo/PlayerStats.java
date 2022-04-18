package grn.database.pojo;


import grn.database.QueryRow;
import org.json.simple.JSONObject;

public class PlayerStats {

  private long playerId;
  private String queueType;
  private String tier;
  private String rank;
  private long leaguePoints;
  private long wins;
  private long loses;

  public void fromJson (JSONObject jStats) {
    this.queueType = (String) jStats.get("queueType");
    this.tier = (String) jStats.get("tier");
    this.rank = (String) jStats.get("rank");
    this.leaguePoints = (long) jStats.get("leaguePoints");
    this.wins = (long) jStats.get("wins");
    this.loses = (long) jStats.get("losses");
  }

  public void fromQueryRow (QueryRow row) {
    this.playerId = (long) row.get(1);
    this.queueType = (String) row.get(2);
    this.tier = (String) row.get(3);
    this.rank = (String) row.get(4);
    this.leaguePoints = (long) row.get(5);
    this.wins = (long) row.get(6);
    this.loses = (long) row.get(7);
  }

  public long getPlayerId() {
    return playerId;
  }

  public void setPlayerId(long playerId) {
    this.playerId = playerId;
  }


  public String getQueueType() {
    return queueType;
  }

  public void setQueueType(String queueType) {
    this.queueType = queueType;
  }


  public String getTier() {
    return tier;
  }

  public void setTier(String tier) {
    this.tier = tier;
  }


  public String getRank() {
    return rank;
  }

  public void setRank(String rank) {
    this.rank = rank;
  }


  public long getLeaguePoints() {
    return leaguePoints;
  }

  public void setLeaguePoints(long leaguePoints) {
    this.leaguePoints = leaguePoints;
  }


  public long getWins() {
    return wins;
  }

  public void setWins(long wins) {
    this.wins = wins;
  }


  public long getLoses() {
    return loses;
  }

  public void setLoses(long loses) {
    this.loses = loses;
  }

}
