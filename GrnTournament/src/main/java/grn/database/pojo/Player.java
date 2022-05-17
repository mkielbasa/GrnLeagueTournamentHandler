package grn.database.pojo;


import grn.database.QueryRow;
import grn.database.repository.Repositories;
import grn.database.repository.TeamRepository;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Player implements Comparable<Player>{

  private long internalId;
  private String id;
  private String accountId;
  private String pUuid;
  private String name;
  private long teamId;
  private long profileIconId;
  private long revisionDate;
  private long summonerLevel;
  private PlayerStats playerStats;
  private String tierIcon;
  private String tier;
  private String profileIcon;
  private String teamIcon;
  private String winRatio;
  private int tierValue;
  private boolean active;
  private List<ChampionMastery> masteries = new ArrayList<>();

  public void fromJson (JSONObject jPlayer) {
      this.id = (String)jPlayer.get("id");
      this.accountId = (String) jPlayer.get("accountId");
      this.pUuid = (String) jPlayer.get("puuid");
      this.name = (String) jPlayer.get("name");
      this.profileIconId = (long) jPlayer.get("profileIconId");
      this.revisionDate = (long) jPlayer.get("revisionDate");
      this.summonerLevel = (long) jPlayer.get("summonerLevel");
      this.active = true;
  }

  public void fromQueryRow (QueryRow row) {
      this.internalId = (long) row.get(1);
      this.accountId = (String) row.get(2);
      this.pUuid = (String) row.get(3);
      this.name = (String) row.get(4);
      this.teamId = (long) row.get(5);
      this.profileIconId = (long) row.get(6);
      this.revisionDate = (long) row.get(7);
      this.summonerLevel = (long) row.get(8);
      this.id = (String) row.get(9);
      this.active = (Boolean) row.get(10);
      this.profileIcon = "/profileicon/" + profileIconId + ".png";
      updateTeamIcon();
  }

  private void updateTeamIcon () {
      TeamRepository teamRepository = Repositories.getTeamRepository();
      Team team = teamRepository.getTeam(teamId);
      if (team == null)
          return;
      teamIcon = team.getIcon();
  }

  public void toggleActive () {
      this.active = !active;
  }

    public boolean isActive() {
        return active;
    }

    public String getWinRatio () {
      if (playerStats == null)
          return "???";
      long wins = playerStats.getWins();
      long loses = playerStats.getLoses();
      long matches = wins + loses;
      double ratio = ((double)wins/(double)matches) * 100;
      return String.format("%,.2f", ratio) + "%";
  }

    public int getTierValue() {
        return tierValue;
    }

    public String getTeamIcon() {
        return teamIcon;
    }

    public String getProfileIcon() {
        return profileIcon;
    }

    public List<ChampionMastery> getMasteries() {
        return masteries;
    }

    public void setMasteries(List<ChampionMastery> masteries) {
        this.masteries = masteries;
    }

    public String getTierIcon() {
        return tierIcon;
    }

    public void setTierIcon(String tierIcon) {
        this.tierIcon = tierIcon;
    }

    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    public void setPlayerStats(PlayerStats playerStats) {
        this.playerStats = playerStats;
        this.tierIcon = PlayerRank.getRankIcon(playerStats.getTier());
        this.tier = playerStats.getTier() + " " + playerStats.getRank();
        this.winRatio = getWinRatio();
        int rankValue = PlayerRank.getRankValue(playerStats.getTier());
        this.tierValue = (rankValue*400) + (int)playerStats.getLeaguePoints();
    }

    public String getTier() {
        return tier;
    }

    public long getProfileIconId() {
    return profileIconId;
  }

  public long getRevisionDate() {
    return revisionDate;
  }

  public long getSummonerLevel() {
    return summonerLevel;
  }

  public long getInternalId() {
    return internalId;
  }

  public String getSummonerId() {
    return id;
  }

  public String getAccountId() {
    return accountId;
  }

  public String getPUuid() {
    return pUuid;
  }

  public String getName() {
    return name;
  }

  public long getTeamId() {
    return teamId;
  }

  public void setTeamId(long teamId) {
    this.teamId = teamId;
  }

    public void setInternalId(long internalId) {
        this.internalId = internalId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setpUuid(String pUuid) {
        this.pUuid = pUuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileIconId(long profileIconId) {
        this.profileIconId = profileIconId;
    }

    public void setRevisionDate(long revisionDate) {
        this.revisionDate = revisionDate;
    }

    public void setSummonerLevel(long summonerLevel) {
        this.summonerLevel = summonerLevel;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public void setProfileIcon(String profileIcon) {
        this.profileIcon = profileIcon;
    }

    public void setTeamIcon(String teamIcon) {
        this.teamIcon = teamIcon;
    }

    public void setWinRatio(String winRatio) {
        this.winRatio = winRatio;
    }

    @Override
    public int compareTo(@NotNull Player p) {
        return Integer.compare(tierValue, p.tierValue);
    }
}
