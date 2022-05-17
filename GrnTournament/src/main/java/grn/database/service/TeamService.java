package grn.database.service;

import grn.database.*;
import grn.database.pojo.Player;
import grn.database.pojo.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamService {

    public static List<Team> getAllTeams () {
        List<Team> teams = new ArrayList<>();
        String sql = "select * from tournament.team";
        Query query = new Query(sql);
        List<QueryRow> rows = query.execute();
        for (QueryRow row : rows) {
            Team team = new Team();
            team.fromQueryRow(row);
            teams.add(team);
        }
        return teams;
    }

    public static boolean teamRegistered (String name) {
        String sql = "select * from tournament.team where name = ?";
        Query query = new Query(sql);
        query.setParams(name);
        List<QueryRow> rows = query.execute();
        return rows.size() == 1;
    }

    public static void register (Team team) {
        Insert insert = new Insert("tournament.team");
        insert.setColumns("name", "shortName", "icon");
        insert.setValues(team.getName(), team.getShortName(), team.getIcon());
        insert.execute();
    }

    public static void update(Team team) {
        String sql = "update tournament.team set name=?,shortName=?,active=? where id=?";
        Update update = new Update(sql);
        update.setParams(team.getName(), team.getShortName(), team.isActive(), team.getId());
        update.execute();
    }

    public static void unregister(long internalId) {
        String sql = "delete from tournament.team where id=?";
        Delete delete = new Delete(sql);
        delete.setParams(internalId);
        delete.execute();
    }
}
