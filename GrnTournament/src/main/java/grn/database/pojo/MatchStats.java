package grn.database.pojo;


public class MatchStats {

  private long matchId;
  private long gameCreation;
  private long duration;
  private long winnerId;


  public long getMatchId() {
    return matchId;
  }

  public void setMatchId(long matchId) {
    this.matchId = matchId;
  }


  public long getGameCreation() {
    return gameCreation;
  }

  public void setGameCreation(long gameCreation) {
    this.gameCreation = gameCreation;
  }


  public long getDuration() {
    return duration;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }


  public long getWinnerId() {
    return winnerId;
  }

  public void setWinnerId(long winnerId) {
    this.winnerId = winnerId;
  }

}
