package grn.database.pojo;


public class TournamentTable {

  private long teamId;
  private long points;
  private long wins;
  private long loses;


  public long getTeamId() {
    return teamId;
  }

  public void setTeamId(long teamId) {
    this.teamId = teamId;
  }


  public long getPoints() {
    return points;
  }

  public void setPoints(long points) {
    this.points = points;
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
