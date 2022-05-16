package grn.database.repository;

import grn.database.pojo.Player;
import grn.database.pojo.Team;
import grn.database.service.TeamService;
import grn.error.ConsoleHandler;

import java.io.File;
import java.util.*;

public class TeamRepository implements Repository {
    private Map<Long, Team> teams = new HashMap<>();
    private static final File TEAMS_FILE = new File("./GrnTournament/teams.conf");

    @Override
    public void init() {
        reloadTeams();
    }

    @Override
    public void reload() {
        reloadTeams();
    }

    private void reloadTeams() {
        ConsoleHandler.handleInfo("Building teams repository");
        teams.clear();
        List<Team> registeredTeams = TeamService.getAllTeams();
        for (Team team :registeredTeams)
            teams.put(team.getId(), team);
    }

    public List<Team> getAllTeams () {
        return new ArrayList<>(teams.values());
    }

    public Team getTeam (String teamName) {
        for (Team team : teams.values()) {
            if (team.getName().equals(teamName))
                return team;
        }
        return null;
    }

    public Player getPlayerByWord (String playerName) {
        for (Team team : teams.values()) {
            for (Player player : team.getPlayers()) {
                if (player.getName().equalsIgnoreCase(playerName)
                        || player.getName().toLowerCase(Locale.ROOT).contains(playerName.toLowerCase(Locale.ROOT)))
                    return player;
            }
        }
        return null;
    }

    public Team getTeamByWord (String teamName) {
        for (Team team : teams.values()) {
            if (team.getName().equalsIgnoreCase(teamName)
                    || team.getShortName().equalsIgnoreCase(teamName)
                    || team.getName().toLowerCase(Locale.ROOT).contains(teamName.toLowerCase(Locale.ROOT)))
                return team;
        }
        return null;
    }

    public Team getTeam (long id) {
        for (Team team : teams.values()) {
            if (team.getId() == id)
                return team;
        }
        return null;
    }

}
