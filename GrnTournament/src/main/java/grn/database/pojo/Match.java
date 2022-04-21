package grn.database.pojo;


import grn.database.QueryRow;

public class Match {

  private long id;
  private long teamA;
  private long teamB;
  private boolean finished;
  private int queue;
  private String matchId;
  private int participants;

  public void fromQueryRow (QueryRow row) {
    this.id = (long) row.get(1);
    this.teamA = (long) row.get(2);
    this.teamB = (long) row.get(3);
    this.finished = (boolean) row.get(4);
    this.queue = (int) row.get(5);
    this.matchId = (String) row.get(6);
    this.participants = (int) row.get(7);
  }

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


  public boolean isFinished() {
    return finished;
  }

  public void setFinished(boolean finished) {
    this.finished = finished;
  }


  public int getQueue() {
    return queue;
  }

  public void setQueue(int queue) {
    this.queue = queue;
  }


  public String getMatchId() {
    return matchId;
  }

  public void setMatchId(String matchId) {
    this.matchId = matchId;
  }

  public int getParticipants() {
    return participants;
  }

  public void setParticipants(int participants) {
    this.participants = participants;
  }
}
