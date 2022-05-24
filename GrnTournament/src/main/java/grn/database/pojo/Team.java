package grn.database.pojo;


import grn.database.QueryRow;
import grn.database.repository.Repositories;
import grn.database.repository.TeamRepository;
import grn.database.service.TeamService;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class Team implements  Comparable<Team> {

  private long id;
  private String name;
  private String shortName;
  private String icon;
  private String winRatio;
  private boolean active;
  private int extraLP;

  LinkedHashMap<Long, TeamHistory> history = new LinkedHashMap<>();
  private List<Player> players = new ArrayList<>();

  public void fromQueryRow(QueryRow row) {
    this.id = (long) row.get(1);
    this.name = (String) row.get(2);
    this.shortName = (String) row.get(3);
    this.active = (Boolean) row.get(5);
    this.extraLP = (int) row.get(6);
    this.icon = shortName + ".png";
    this.history = TeamService.getTeamHistory(id);
  }

  public int getExtraLP() {
    return extraLP;
  }

  public LinkedHashMap<Long, TeamHistory> getHistory() {
    return history;
  }

  public List<String> getScreens () {
    List<String> screens = new LinkedList<>();
    TeamRepository teamRepository = Repositories.getTeamRepository();
    int screenCount = teamRepository.getScreensCount();
    int ownScreenCount = history.keySet().size();
    if (ownScreenCount < screenCount)
      for (int i=0; i < (screenCount - ownScreenCount); i++)
        screens.add("'NA'");
    for (Long time : history.keySet()) {
      Timestamp t = new Timestamp(time);
      String s = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(t);
      screens.add("'" + s + "'");
    }
    return screens;
  }

  public List<Long> getLpHistory () {
    List<Long> l = new LinkedList<>();
    TeamRepository teamRepository = Repositories.getTeamRepository();
    int screenCount = teamRepository.getScreensCount();
    int ownScreenCount = history.keySet().size();
    if (ownScreenCount < screenCount)
      for (int i=0; i < (screenCount - ownScreenCount); i++)
        l.add(0l);
    for (TeamHistory ph : history.values())
      l.add (ph.getLp());
    return l;
  }

  public List<Integer> getWinHistory () {
    List<Integer> l = new LinkedList<>();
    TeamRepository teamRepository = Repositories.getTeamRepository();
    int screenCount = teamRepository.getScreensCount();
    int ownScreenCount = history.keySet().size();
    if (ownScreenCount < screenCount)
      for (int i=0; i < (screenCount - ownScreenCount); i++)
        l.add(0);
    for (TeamHistory ph : history.values())
      l.add (ph.getWins());
    return l;
  }

  public List<Integer> getLosesHistory () {
    List<Integer> l = new LinkedList<>();
    TeamRepository teamRepository = Repositories.getTeamRepository();
    int screenCount = teamRepository.getScreensCount();
    int ownScreenCount = history.keySet().size();
    if (ownScreenCount < screenCount)
      for (int i=0; i < (screenCount - ownScreenCount); i++)
        l.add(0);
    for (TeamHistory ph : history.values())
      l.add (ph.getLoses());
    return l;
  }

  public List<Integer> getMatchesHistory () {
    List<Integer> l = new LinkedList<>();
    TeamRepository teamRepository = Repositories.getTeamRepository();
    int screenCount = teamRepository.getScreensCount();
    int ownScreenCount = history.keySet().size();
    if (ownScreenCount < screenCount)
      for (int i=0; i < (screenCount - ownScreenCount); i++)
        l.add(0);
    for (TeamHistory ph : history.values())
      l.add (ph.getMatches());
    return l;
  }

  public boolean containsPlayer (String pUUID) {
    for (Player player : players)
      if (player.getPUuid().equals(pUUID))
        return true;
    return false;
  }

  public void toggleActive () {
    this.active = !active;
  }

  public boolean isActive() {
    return active;
  }

  public int getTeamTierValue () {
      Collections.sort(players);
      Collections.reverse(players);
      int tierValue = 0;
      for (int i=0; i<players.size(); i++) {
        if (i >= 5)
          continue;
        Player player = players.get(i);
        tierValue += player.getTierValue();
      }
      tierValue += extraLP;
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

  public String getShortNameFormat() {
    return "'" + shortName + "'";
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

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Team that = (Team) o;
      return id == that.id;
  }

  @Override
  public int compareTo(@NotNull Team t) {
    return Integer.compare(getTeamTierValue(), t.getTeamTierValue());
  }
}
