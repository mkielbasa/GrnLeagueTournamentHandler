package grn.database.pojo;


import grn.database.QueryRow;
import grn.properties.json.JsonParser;
import org.json.simple.JSONObject;

public class PlayerMatchStats {

  private long playerId;
  private long teamId;
  private long matchId;
  private long assists;
  private long baronKills;
  private long champLevel;
  private long championId;
  private long damageDealtToBuildings;
  private long damageDealtToObjectives;
  private long damageDealtToTurrets;
  private long damageSelfMitigated;
  private long deaths;
  private long detectorWardsPlaced;
  private long dragonKills;
  private boolean firstBloodKill;
  private boolean firstTowerKill;
  private long goldEarned;
  private long goldSpent;
  private String individualPosition;
  private long inhibitorKills;
  private long inhibitorTakedowns;
  private long inhibitorsLost;
  private long item0;
  private long item1;
  private long item2;
  private long item3;
  private long item4;
  private long item5;
  private long item6;
  private long kills;
  private long magicDamageDealt;
  private long magicDamageDealtToChampions;
  private long neutralMinionsKilled;
  private long objectivesStolen;
  private long physicalDamageDealt;
  private long physicalDamageDealtToChampions;
  private long profileIcon;
  private long spell1Casts;
  private long spell2Casts;
  private long spell3Casts;
  private long spell4Casts;
  private long totalDamageDealt;
  private long totalDamageDealtToChampions;
  private long totalHeal;
  private long totalHealsOnTeammates;
  private long totalMinionsKilled;
  private long totalTimeCcDealt;
  private long totalTimeSpentDead;
  private long trueDamageDealt;
  private long trueDamageDealtToChampions;
  private long turretKills;
  private long visionScore;
  private long nexusKills;

  public void fromJson (JSONObject jStats) {
    this.assists = (long) jStats.get("assists");
    this.baronKills = (long) jStats.get("baronKills");
    this.champLevel = (long) jStats.get("champLevel");
    this.championId = (long) jStats.get("championId");
    this.damageDealtToBuildings = (long) jStats.get ("damageDealtToBuildings");
    this.damageDealtToObjectives = (long) jStats.get("damageDealtToObjectives");
    this.damageDealtToTurrets = (long) jStats.get("damageDealtToTurrets");
    this.damageSelfMitigated = (long) jStats.get("damageSelfMitigated");
    this.deaths = (long) jStats.get("deaths");
    this.detectorWardsPlaced = (long) jStats.get("detectorWardsPlaced");
    this.dragonKills = (long) jStats.get("dragonKills");
    this.firstBloodKill = (boolean) jStats.get("firstBloodKill");
    this.firstTowerKill = (boolean) jStats.get("firstTowerKill");
    this.goldEarned = (long) jStats.get("goldEarned");
    this.goldSpent = (long) jStats.get("goldSpent");
    this.individualPosition = (String) jStats.get("individualPosition");
    this.inhibitorKills = (long) jStats.get("inhibitorKills");
    this.inhibitorTakedowns = (long) jStats.get("inhibitorTakedowns");
    this.inhibitorsLost = (long) jStats.get("inhibitorsLost");
    this.item0 = (long) jStats.get("item0");
    this.item1 = (long) jStats.get("item1");
    this.item2 = (long) jStats.get("item2");
    this.item3 = (long) jStats.get("item3");
    this.item4 = (long) jStats.get("item4");
    this.item5 = (long) jStats.get("item5");
    this.item6 = (long) jStats.get("item6");
    this.kills = (long) jStats.get("kills");
    this.magicDamageDealt = (long) jStats.get("magicDamageDealt");
    this.magicDamageDealtToChampions = (long) jStats.get("magicDamageDealtToChampions");
    this.neutralMinionsKilled = (long) jStats.get("neutralMinionsKilled");
    this.objectivesStolen = (long) jStats.get("objectivesStolen");
    this.physicalDamageDealt = (long) jStats.get("physicalDamageDealt");
    this.physicalDamageDealtToChampions = (long) jStats.get("physicalDamageDealtToChampions");
    this.profileIcon = (long) jStats.get("profileIcon");
    this.spell1Casts = (long) jStats.get("spell1Casts");
    this.spell2Casts = (long) jStats.get("spell2Casts");
    this.spell3Casts = (long) jStats.get("spell3Casts");
    this.spell4Casts = (long) jStats.get("spell4Casts");
    this.totalDamageDealt = (long) jStats.get("totalDamageDealt");
    this.totalDamageDealtToChampions = (long) jStats.get("totalDamageDealtToChampions");
    this.totalHeal = (long) jStats.get("totalHeal");
    this.totalHealsOnTeammates = (long) jStats.get("totalHealsOnTeammates");
    this.totalMinionsKilled = (long) jStats.get("totalMinionsKilled");
    this.totalTimeCcDealt = (long) jStats.get("totalTimeCCDealt");
    this.totalTimeSpentDead = (long) jStats.get("totalTimeSpentDead");
    this.trueDamageDealt = (long) jStats.get("trueDamageDealt");
    this.trueDamageDealtToChampions = (long) jStats.get("trueDamageDealtToChampions");
    this.turretKills = (long) jStats.get("turretKills");
    this.visionScore = (long) jStats.get("visionScore");
    this.nexusKills = (long) jStats.get("nexusKills");
  }

  public void fromQueryRow (QueryRow row) {
    this.assists = (long) row.get(1);
    this.baronKills = (long) row.get(2);
    this.champLevel = (long) row.get(3);
    this.championId = (long) row.get(4);
    this.damageDealtToBuildings = (long) row.get(5);
    this.damageDealtToObjectives = (long) row.get(6);
    this.damageDealtToTurrets = (long) row.get(7);
    this.damageSelfMitigated = (long) row.get(8);
    this.deaths = (long) row.get(9);
    this.detectorWardsPlaced = (long) row.get(10);
    this.dragonKills = (long) row.get(11);
    this.firstBloodKill = (boolean) row.get(12);
    this.firstTowerKill = (boolean) row.get(13);
    this.goldEarned = (long) row.get(14);
    this.goldSpent = (long) row.get(15);
    this.individualPosition = (String) row.get(16);
    this.inhibitorKills = (long) row.get(17);
    this.inhibitorTakedowns = (long) row.get(18);
    this.inhibitorsLost = (long) row.get(19);
    this.item0 = (long) row.get(20);
    this.item1 = (long) row.get(21);
    this.item2 = (long) row.get(22);
    this.item3 = (long) row.get(23);
    this.item4 = (long) row.get(24);
    this.item5 = (long) row.get(25);
    this.item6 = (long) row.get(26);
    this.kills = (long) row.get(27);
    this.magicDamageDealt = (long) row.get(28);
    this.magicDamageDealtToChampions = (long) row.get(29);
    this.neutralMinionsKilled = (long) row.get(30);
    this.objectivesStolen = (long) row.get(31);
    this.physicalDamageDealt = (long) row.get(32);
    this.physicalDamageDealtToChampions = (long) row.get(33);
    this.profileIcon = (long) row.get(34);
    this.spell1Casts = (long) row.get(35);
    this.spell2Casts = (long) row.get(36);
    this.spell3Casts = (long) row.get(37);
    this.spell4Casts = (long) row.get(38);
    this.totalDamageDealt = (long) row.get(39);
    this.totalHeal = (long) row.get(40);
    this.totalHealsOnTeammates = (long) row.get(41);
    this.totalMinionsKilled = (long) row.get(42);
    this.totalTimeCcDealt = (long) row.get(43);
    this.totalTimeSpentDead = (long) row.get(44);
    this.trueDamageDealt = (long) row.get(45);
    this.trueDamageDealtToChampions = (long) row.get(46);
    this.turretKills = (long) row.get(47);
    this.visionScore = (long) row.get(48);
    this.nexusKills = (long) row.get(49);
  }


  public long getPlayerId() {
    return playerId;
  }

  public void setPlayerId(long playerId) {
    this.playerId = playerId;
  }


  public long getTeamId() {
    return teamId;
  }

  public void setTeamId(long teamId) {
    this.teamId = teamId;
  }


  public long getMatchId() {
    return matchId;
  }

  public void setMatchId(long matchId) {
    this.matchId = matchId;
  }


  public long getAssists() {
    return assists;
  }

  public void setAssists(long assists) {
    this.assists = assists;
  }


  public long getBaronKills() {
    return baronKills;
  }

  public void setBaronKills(long baronKills) {
    this.baronKills = baronKills;
  }


  public long getChampLevel() {
    return champLevel;
  }

  public void setChampLevel(long champLevel) {
    this.champLevel = champLevel;
  }


  public long getChampionId() {
    return championId;
  }

  public void setChampionId(long championId) {
    this.championId = championId;
  }


  public long getDamageDealtToBuildings() {
    return damageDealtToBuildings;
  }

  public void setDamageDealtToBuildings(long damageDealtToBuildings) {
    this.damageDealtToBuildings = damageDealtToBuildings;
  }


  public long getDamageDealtToObjectives() {
    return damageDealtToObjectives;
  }

  public void setDamageDealtToObjectives(long damageDealtToObjectives) {
    this.damageDealtToObjectives = damageDealtToObjectives;
  }


  public long getDamageDealtToTurrets() {
    return damageDealtToTurrets;
  }

  public void setDamageDealtToTurrets(long damageDealtToTurrets) {
    this.damageDealtToTurrets = damageDealtToTurrets;
  }


  public long getDamageSelfMitigated() {
    return damageSelfMitigated;
  }

  public void setDamageSelfMitigated(long damageSelfMitigated) {
    this.damageSelfMitigated = damageSelfMitigated;
  }


  public long getDeaths() {
    return deaths;
  }

  public void setDeaths(long deaths) {
    this.deaths = deaths;
  }


  public long getDetectorWardsPlaced() {
    return detectorWardsPlaced;
  }

  public void setDetectorWardsPlaced(long detectorWardsPlaced) {
    this.detectorWardsPlaced = detectorWardsPlaced;
  }


  public long getDragonKills() {
    return dragonKills;
  }

  public void setDragonKills(long dragonKills) {
    this.dragonKills = dragonKills;
  }


  public boolean getFirstBloodKill() {
    return firstBloodKill;
  }

  public void setFirstBloodKill(boolean firstBloodKill) {
    this.firstBloodKill = firstBloodKill;
  }


  public boolean getFirstTowerKill() {
    return firstTowerKill;
  }

  public void setFirstTowerKill(boolean firstTowerKill) {
    this.firstTowerKill = firstTowerKill;
  }


  public long getGoldEarned() {
    return goldEarned;
  }

  public void setGoldEarned(long goldEarned) {
    this.goldEarned = goldEarned;
  }


  public long getGoldSpent() {
    return goldSpent;
  }

  public void setGoldSpent(long goldSpent) {
    this.goldSpent = goldSpent;
  }


  public String getIndividualPosition() {
    return individualPosition;
  }

  public void setIndividualPosition(String individualPosition) {
    this.individualPosition = individualPosition;
  }


  public long getInhibitorKills() {
    return inhibitorKills;
  }

  public void setInhibitorKills(long inhibitorKills) {
    this.inhibitorKills = inhibitorKills;
  }


  public long getInhibitorTakedowns() {
    return inhibitorTakedowns;
  }

  public void setInhibitorTakedowns(long inhibitorTakedowns) {
    this.inhibitorTakedowns = inhibitorTakedowns;
  }


  public long getInhibitorsLost() {
    return inhibitorsLost;
  }

  public void setInhibitorsLost(long inhibitorsLost) {
    this.inhibitorsLost = inhibitorsLost;
  }


  public long getItem0() {
    return item0;
  }

  public void setItem0(long item0) {
    this.item0 = item0;
  }


  public long getItem1() {
    return item1;
  }

  public void setItem1(long item1) {
    this.item1 = item1;
  }


  public long getItem2() {
    return item2;
  }

  public void setItem2(long item2) {
    this.item2 = item2;
  }


  public long getItem3() {
    return item3;
  }

  public void setItem3(long item3) {
    this.item3 = item3;
  }


  public long getItem4() {
    return item4;
  }

  public void setItem4(long item4) {
    this.item4 = item4;
  }


  public long getItem5() {
    return item5;
  }

  public void setItem5(long item5) {
    this.item5 = item5;
  }


  public long getItem6() {
    return item6;
  }

  public void setItem6(long item6) {
    this.item6 = item6;
  }


  public long getKills() {
    return kills;
  }

  public void setKills(long kills) {
    this.kills = kills;
  }


  public long getMagicDamageDealt() {
    return magicDamageDealt;
  }

  public void setMagicDamageDealt(long magicDamageDealt) {
    this.magicDamageDealt = magicDamageDealt;
  }


  public long getMagicDamageDealtToChampions() {
    return magicDamageDealtToChampions;
  }

  public void setMagicDamageDealtToChampions(long magicDamageDealtToChampions) {
    this.magicDamageDealtToChampions = magicDamageDealtToChampions;
  }


  public long getNeutralMinionsKilled() {
    return neutralMinionsKilled;
  }

  public void setNeutralMinionsKilled(long neutralMinionsKilled) {
    this.neutralMinionsKilled = neutralMinionsKilled;
  }


  public long getObjectivesStolen() {
    return objectivesStolen;
  }

  public void setObjectivesStolen(long objectivesStolen) {
    this.objectivesStolen = objectivesStolen;
  }


  public long getPhysicalDamageDealt() {
    return physicalDamageDealt;
  }

  public void setPhysicalDamageDealt(long physicalDamageDealt) {
    this.physicalDamageDealt = physicalDamageDealt;
  }


  public long getPhysicalDamageDealtToChampions() {
    return physicalDamageDealtToChampions;
  }

  public void setPhysicalDamageDealtToChampions(long physicalDamageDealtToChampions) {
    this.physicalDamageDealtToChampions = physicalDamageDealtToChampions;
  }


  public long getProfileIcon() {
    return profileIcon;
  }

  public void setProfileIcon(long profileIcon) {
    this.profileIcon = profileIcon;
  }


  public long getSpell1Casts() {
    return spell1Casts;
  }

  public void setSpell1Casts(long spell1Casts) {
    this.spell1Casts = spell1Casts;
  }


  public long getSpell2Casts() {
    return spell2Casts;
  }

  public void setSpell2Casts(long spell2Casts) {
    this.spell2Casts = spell2Casts;
  }


  public long getSpell3Casts() {
    return spell3Casts;
  }

  public void setSpell3Casts(long spell3Casts) {
    this.spell3Casts = spell3Casts;
  }


  public long getSpell4Casts() {
    return spell4Casts;
  }

  public void setSpell4Casts(long spell4Casts) {
    this.spell4Casts = spell4Casts;
  }


  public long getTotalDamageDealt() {
    return totalDamageDealt;
  }

  public void setTotalDamageDealt(long totalDamageDealt) {
    this.totalDamageDealt = totalDamageDealt;
  }


  public long getTotalDamageDealtToChampions() {
    return totalDamageDealtToChampions;
  }

  public void setTotalDamageDealtToChampions(long totalDamageDealtToChampions) {
    this.totalDamageDealtToChampions = totalDamageDealtToChampions;
  }


  public long getTotalHeal() {
    return totalHeal;
  }

  public void setTotalHeal(long totalHeal) {
    this.totalHeal = totalHeal;
  }


  public long getTotalHealsOnTeammates() {
    return totalHealsOnTeammates;
  }

  public void setTotalHealsOnTeammates(long totalHealsOnTeammates) {
    this.totalHealsOnTeammates = totalHealsOnTeammates;
  }


  public long getTotalMinionsKilled() {
    return totalMinionsKilled;
  }

  public void setTotalMinionsKilled(long totalMinionsKilled) {
    this.totalMinionsKilled = totalMinionsKilled;
  }


  public long getTotalTimeCcDealt() {
    return totalTimeCcDealt;
  }

  public void setTotalTimeCcDealt(long totalTimeCcDealt) {
    this.totalTimeCcDealt = totalTimeCcDealt;
  }


  public long getTotalTimeSpentDead() {
    return totalTimeSpentDead;
  }

  public void setTotalTimeSpentDead(long totalTimeSpentDead) {
    this.totalTimeSpentDead = totalTimeSpentDead;
  }


  public long getTrueDamageDealt() {
    return trueDamageDealt;
  }

  public void setTrueDamageDealt(long trueDamageDealt) {
    this.trueDamageDealt = trueDamageDealt;
  }


  public long getTrueDamageDealtToChampions() {
    return trueDamageDealtToChampions;
  }

  public void setTrueDamageDealtToChampions(long trueDamageDealtToChampions) {
    this.trueDamageDealtToChampions = trueDamageDealtToChampions;
  }


  public long getTurretKills() {
    return turretKills;
  }

  public void setTurretKills(long turretKills) {
    this.turretKills = turretKills;
  }


  public long getVisionScore() {
    return visionScore;
  }

  public void setVisionScore(long visionScore) {
    this.visionScore = visionScore;
  }


  public long getNexusKills() {
    return nexusKills;
  }

  public void setNexusKills(long nexusKills) {
    this.nexusKills = nexusKills;
  }

}
