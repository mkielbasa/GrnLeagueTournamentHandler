package grn.database.pojo;


public class PlayerStats {

  private long playerId;
  private String queueType;
  private String tier;
  private String rank;
  private long leaguePoints;
  private long wins;
  private long loses;


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
