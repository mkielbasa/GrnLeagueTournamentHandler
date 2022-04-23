package grn.database.pojo;


import grn.database.QueryRow;

public class MatchStats implements Comparable<MatchStats> {

  private long teamId;
  private String teamIcon;
  private String teamName;
  private long goldEarned;
  private long wins;
  private long matchesDuration;
  private long goldForMinute;

  public void fromQueryRow (QueryRow row) {
    this.teamId = (long) row.get(1);
    this.goldEarned = (long) row.get(3);
    this.wins = (long) row.get(2);
  }

  @Override public int compareTo(MatchStats u) {
    if (wins == u.wins) {
      return Long.compare(goldForMinute, u.goldForMinute);
    } else {
      if (wins > u.wins)
        return 1;
      else
        return -1;
    }
  }

  public String getTeamIcon() {
    return teamIcon;
  }

  public void setTeamIcon(String teamIcon) {
    this.teamIcon = teamIcon;
  }

  public String getTeamName() {
    return teamName;
  }

  public void setTeamName(String teamName) {
    this.teamName = teamName;
  }

  public long getGoldForMinute() {
    return goldForMinute;
  }

  public void setGoldForMinute(long goldForMinute) {
    this.goldForMinute = goldForMinute;
  }

  public long getMatchesDuration() {
    return matchesDuration;
  }

  public void setMatchesDuration(long matchesDuration) {
    this.matchesDuration = matchesDuration;
  }

  public long getTeamId() {
    return teamId;
  }

  public void setTeamId(long teamId) {
    this.teamId = teamId;
  }


  public long getGoldEarned() {
    return goldEarned;
  }

  public void setGoldEarned(long goldEarned) {
    this.goldEarned = goldEarned;
  }

  public long getWins() {
    return wins;
  }

  public void setWins(long wins) {
    this.wins = wins;
  }

}
