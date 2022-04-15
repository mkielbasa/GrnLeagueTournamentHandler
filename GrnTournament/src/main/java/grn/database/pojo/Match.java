package grn.database.pojo;


public class Match {

  private long id;
  private long teamA;
  private long teamB;
  private String finished;
  private long queue;
  private String matchId;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getTeamA() {
    return teamA;
  }

  public void setTeamA(long teamA) {
    this.teamA = teamA;
  }


  public long getTeamB() {
    return teamB;
  }

  public void setTeamB(long teamB) {
    this.teamB = teamB;
  }


  public String getFinished() {
    return finished;
  }

  public void setFinished(String finished) {
    this.finished = finished;
  }


  public long getQueue() {
    return queue;
  }

  public void setQueue(long queue) {
    this.queue = queue;
  }


  public String getMatchId() {
    return matchId;
  }

  public void setMatchId(String matchId) {
    this.matchId = matchId;
  }

}
