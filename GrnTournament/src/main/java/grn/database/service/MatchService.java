package grn.database.service;

import grn.database.*;
import grn.database.pojo.Match;
import grn.database.pojo.PlayerMatchStats;
import grn.database.pojo.Team;

import java.util.ArrayList;
import java.util.List;

public class MatchService {

    public static void clearMatches () {
        String sql = "delete from tournament.match";
        Delete delete = new Delete(sql);
        delete.execute();
    }

    public static void clearMatchStats () {
        String sql = "delete from tournament.playermatchstats";
        Delete delete = new Delete(sql);
        delete.execute();
    }

    public static void addMatchStats (PlayerMatchStats pms) {
        Insert insert = new Insert("tournament.playermatchstats");
        insert.setColumns("playerid", "teamId", "matchid",
                "assists", "baronkills", "champlevel", "championId",
                "damagedealttobuildings", "damagedealttoobjectives", "damagedealttoturrets",
                "damageselfmitigated", "deaths", "detectorwardsplaced", "dragonKills", "firstbloodkill",
                "firsttowerkill", "goldearned", "goldspent", "individualposition", "inhibitorkills",
                "inhibitortakedowns", "inhibitorslost",
                "item0", "item1", "item2", "item3", "item4", "item5", "item6",
                "kills", "magicdamagedealt", "magicdamagedealttochampions", "neutralminionskilled",
                "objectivesstolen", "physicaldamagedealt", "physicaldamagedealttochampions", "profileicon",
                "spell1casts", "spell2casts", "spell3casts", "spell4casts", "totaldamagedealt",
                "totaldamagedealttochampions", "totalheal", "totalhealsonteammates", "totalminionskilled",
                "totaltimeccdealt", "totaltimespentdead", "truedamagedealt", "truedamagedealttochampions",
                "turretkills", "visionscore", "nexuskills");
        insert.setValues(pms.getPlayerId(), pms.getTeamId(), pms.getMatchId(),
                pms.getAssists(), pms.getBaronKills(), pms.getChampLevel(), pms.getChampionId(),
                pms.getDamageDealtToBuildings(), pms.getDamageDealtToObjectives(), pms.getDamageDealtToTurrets(),
                pms.getDamageSelfMitigated(), pms.getDeaths(), pms.getDetectorWardsPlaced(),
                pms.getDragonKills(), pms.getFirstBloodKill(), pms.getFirstTowerKill(), pms.getGoldEarned(),
                pms.getGoldSpent(), pms.getIndividualPosition(), pms.getInhibitorKills(), pms.getInhibitorTakedowns(),
                pms.getInhibitorsLost(), pms.getItem0(), pms.getItem1(), pms.getItem2(), pms.getItem3(),
                pms.getItem4(), pms.getItem5(), pms.getItem6(), pms.getKills(), pms.getMagicDamageDealt(),
                pms.getMagicDamageDealtToChampions(), pms.getNeutralMinionsKilled(), pms.getObjectivesStolen(),
                pms.getPhysicalDamageDealt(), pms.getPhysicalDamageDealtToChampions(), pms.getProfileIcon(),
                pms.getSpell1Casts(), pms.getSpell2Casts(), pms.getSpell3Casts(), pms.getSpell4Casts(),
                pms.getTotalDamageDealt(), pms.getTotalDamageDealtToChampions(), pms.getTotalHeal(),
                pms.getTotalHealsOnTeammates(), pms.getTotalMinionsKilled(), pms.getTotalTimeCcDealt(),
                pms.getTotalTimeSpentDead(), pms.getTrueDamageDealt(), pms.getTrueDamageDealtToChampions(),
                pms.getTurretKills(), pms.getVisionScore(), pms.getNexusKills());
        insert.execute();
    }

    public static List<QueryRow> getAllMatches () {
        String sql = "select * from tournament.match order by queue asc";
        Query query = new Query(sql);
        return query.execute();
    }

    public static List<QueryRow> getFinishedMatches () {
        String sql = "select * from tournament.match where finished = true order by queue asc";
        Query query = new Query(sql);
        return query.execute();
    }

    public static List<QueryRow> getNonFinishedMatches () {
        String sql = "select * from tournament.match where finished = false order by queue asc";
        Query query = new Query(sql);
        return query.execute();
    }

    public static void finishMatch (long id, String matchId) {
        String sql = "update tournament.match set finished=true, matchid = ? where id = ?";
        Update update = new Update(sql);
        update.setParams(matchId, id);
        update.execute();
    }

    public static String getMatchResult (long id, long teamAId, long teamBId) {
        String sql = "select teamid, sum(nexuskills) from tournament.playermatchstats where matchid = ? and teamid = ? group by teamid";
        Query query = new Query(sql);
        query.setParams(id);
        query.setParams(teamAId);
        List<QueryRow> rows = query.execute();
        if (rows.size() != 1)
            return "???";
        QueryRow teamA = rows.get(0);
        long resultTeamA = (long) teamA.get(2);
        query = new Query(sql);
        query.setParams(id);
        query.setParams(teamBId);
        rows = query.execute();
        if (rows.size() != 1)
            return "???";
        QueryRow teamB = rows.get(0);
        long resultTeamB = (long) teamB.get(2);
        return resultTeamA + " : " + resultTeamB;
    }

}
