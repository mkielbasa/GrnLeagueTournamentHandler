package grn.database.service;

import grn.database.*;
import grn.database.pojo.Match;
import grn.database.pojo.MatchStats;
import grn.database.pojo.PlayerMatchStats;
import grn.database.pojo.Team;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MatchService {

    public static List<Match> getAllMatches () {
        List<Match> matches = new ArrayList<>();
        String sql = "select * from tournament.match";
        Query query = new Query(sql);
        List<QueryRow> rows = query.execute();
        for (QueryRow row : rows) {
            Match match = new Match();
            match.fromQueryRow(row);
            matches.add(match);
        }
        return matches;
    }

    public static void clearMatches () {
        String sql = "delete from tournament.match";
        Delete delete = new Delete(sql);
        delete.execute();
    }

    public static long registerMatch (long parentId) {
        Insert insert = new Insert("tournament.match");
        insert.setColumns("finished", "parent");
        insert.setValues(false, parentId);
        return insert.executeReturning();
    }

    public static void updateTeam (long matchId, long teamId, String side) {
        String sql = "update tournament.match set " + side + "=? where id=?";
        Update update = new Update(sql);
        update.setParams(teamId, matchId);
        update.execute();
    }

    public static void updateScore (long matchId, int teamAScore, int teamBScore) {
        String sql = "update tournament.match set teamascore=?,teambscore=? where id=?";
        Update update = new Update(sql);
        update.setParams(teamAScore, teamBScore, matchId);
        update.execute();
    }

    public static void finishMatch (long matchId, Team winner) {
        String sql = "update tournament.match set finished=?,winner=? where id=?";
        Update update = new Update(sql);
        update.setParams(true, winner.getId(), matchId);
        update.execute();
    }

}
