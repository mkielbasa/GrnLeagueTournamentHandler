package grn.database.service;

import grn.database.Delete;
import grn.database.Insert;
import grn.database.Query;
import grn.database.QueryRow;
import grn.database.pojo.ChampionMastery;
import grn.database.pojo.Player;
import grn.database.pojo.PlayerStats;

import java.util.ArrayList;
import java.util.List;

public class PlayerService {

    public static List<Player> getAllPlayers () {
        List<Player> players = new ArrayList<>();
        String sql = "select * from tournament.player";
        Query query = new Query(sql);
        List<QueryRow> rows = query.execute();
        for (QueryRow row : rows) {
            Player player = new Player();
            player.fromQueryRow(row);
            players.add(player);
        }
        return players;
    }

    public static boolean playerRegistered (String pUUID) {
        String sql = "select * from tournament.player where pUUID = ?";
        Query query = new Query(sql);
        query.setParams(pUUID);
        List<QueryRow> rows = query.execute();
        return rows.size() == 1;
    }

    public static void register (Player player) {
        Insert insert = new Insert("tournament.player");
        insert.setColumns("accountId", "pUUID",
                "name", "teamId", "profileIconId", "revisionDate",
                "summonerLevel", "summonerId");
        insert.setValues(player.getAccountId(), player.getPUuid(),
                player.getName(), player.getTeamId(), player.getProfileIconId(), player.getRevisionDate(),
                player.getSummonerLevel(), player.getId());
        insert.execute();
    }

    public static void clearMasteries (Player player) {
        String sql = "delete from tournament.championmastery where playerid = ?";
        Delete delete = new Delete(sql);
        delete.setParams(player.getInternalId());
        delete.execute();
    }

    public static void addMastery (ChampionMastery championMastery) {
        Insert insert = new Insert("tournament.championmastery");
        insert.setColumns("playerid", "championid", "championlevel", "championpoints", "lastplaytime");
        insert.setValues(championMastery.getPlayerId(), championMastery.getChampionId(), championMastery.getChampionLevel(),
                championMastery.getChampionPoints(), championMastery.getLastPlayTime());
        insert.execute();
    }

    public static List<ChampionMastery> getMasteries (long playerId) {
        List<ChampionMastery> masteries = new ArrayList<>();
        String sql = "select * from tournament.championmastery where playerid = ? order by championpoints desc";
        Query query = new Query(sql);
        query.setParams(playerId);
        List<QueryRow> rows = query.execute();
        for (QueryRow row : rows) {
            ChampionMastery mastery = new ChampionMastery();
            mastery.fromQueryRow(row);
            masteries.add(mastery);
        }
        return masteries;
    }

    public static void clearLeagues (Player player) {
        String sql = "delete from tournament.playerstats where playerid = ?";
        Delete delete = new Delete(sql);
        delete.setParams(player.getInternalId());
        delete.execute();
    }

    public static void addLeague (PlayerStats playerStats) {
        Insert insert = new Insert("tournament.playerstats");
        insert.setColumns("playerid", "queuetype", "tier", "rank", "leaguepoints", "wins", "loses");
        insert.setValues(playerStats.getPlayerId(), playerStats.getQueueType(),
                playerStats.getTier(), playerStats.getRank(), playerStats.getLeaguePoints(),
                playerStats.getWins(), playerStats.getLoses());
        insert.execute();
    }

    public static List<PlayerStats> getLeagues (Player player) {
        List<PlayerStats> playerStats = new ArrayList<>();
        String sql = "select * from tournament.playerstats where playerid = ? and queuetype not like '%TFT%'";
        Query query = new Query(sql);
        query.setParams(player.getInternalId());
        List<QueryRow> rows = query.execute();
        for (QueryRow row : rows) {
            PlayerStats playerStat = new PlayerStats();
            playerStat.fromQueryRow(row);
            playerStats.add(playerStat);
        }
        return playerStats;
    }
}
