package grn.database.service;

import grn.database.Insert;
import grn.database.Query;
import grn.database.QueryRow;
import grn.database.pojo.Player;

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
}
