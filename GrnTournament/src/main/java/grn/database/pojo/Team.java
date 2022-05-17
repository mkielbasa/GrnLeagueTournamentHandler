package grn.database.pojo;


import grn.database.QueryRow;

import java.util.ArrayList;
import java.util.List;

public class Team {

  private long id;
  private String name;
  private String shortName;
  private String icon;
  private String winRatio;

  private List<Player> players = new ArrayList<>();

  public void fromQueryRow(QueryRow row) {
    this.id = (long) row.get(1);
    this.name = (String) row.get(2);
    this.shortName = (String) row.get(3);
    this.icon = shortName + ".png";
  }

  public boolean containsPlayer (String pUUID) {
    for (Player player : players)
      if (player.getPUuid().equals(pUUID))
        return true;
    return false;
  }

  public int getTeamTierValue () {
      int tierValue = 0;
      for (Player player : players)
        tierValue += player.getTierValue();
      return tierValue;
  }

  public String getWinratio () {
    long wins = 0;
    long loses = 0;
    long matches = 0;
    for (Player player : players) {
      if (player.getPlayerStats() == null)
        continue;
      PlayerStats playerStats = player.getPlayerStats();
      wins += playerStats.getWins();
      loses += playerStats.getLoses();
      matches += (wins + loses);
    }
    double ratio = ((double)wins/(double)matches) * 100;
    return String.format("%,.2f", ratio) + "%";
  }

  public void addPlayer (Player player) {
    this.players.add(player);
  }

  public List<Player> getPlayers() {
    return players;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }


  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }


  public void updateWinRatio() {
    this.winRatio = getWinratio();
  }
}
