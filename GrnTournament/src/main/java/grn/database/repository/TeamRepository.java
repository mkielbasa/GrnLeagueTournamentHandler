package grn.database.repository;

import grn.database.pojo.Team;
import grn.database.service.PlayerService;
import grn.database.service.TeamService;
import grn.file.TeamReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamRepository {
    private Map<Long, Team> teams = new HashMap<>();
    private static final File TEAMS_FILE = new File("./GrnTournament/teams.conf");

    public TeamRepository () {
        List<String> teamsNames = TeamReader.read(TEAMS_FILE);
        for (String teamName : teamsNames) {
            Team team = new Team();
            team.setName(teamName);
            if (! TeamService.teamRegistered(teamName))
                TeamService.register(team);
        }
        List<Team> registeredTeams = TeamService.getAllTeams();
        for (Team team :registeredTeams)
            teams.put(team.getId(), team);
    }

    public Team getTeam (String teamName) {
        for (Team team : teams.values()) {
            if (team.getName().equals(teamName))
                return team;
        }
        return null;
    }
}
