package grn.database.pojo;


import grn.database.QueryRow;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Player {

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
  private String winRatio;
  private List<ChampionMastery> masteries = new ArrayList<>();

  public void fromJson (JSONObject jPlayer) {
      this.id = (String)jPlayer.get("id");
      this.accountId = (String) jPlayer.get("accountId");
      this.pUuid = (String) jPlayer.get("puuid");
      this.name = (String) jPlayer.get("name");
      this.profileIconId = (long) jPlayer.get("profileIconId");
      this.revisionDate = (long) jPlayer.get("revisionDate");
      this.summonerLevel = (long) jPlayer.get("summonerLevel");
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
      this.profileIcon = "/profileicon/" + profileIconId + ".png";
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

}
