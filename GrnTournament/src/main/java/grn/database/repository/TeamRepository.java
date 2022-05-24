package grn.database.repository;

import grn.database.pojo.*;
import grn.database.service.PlayerService;
import grn.database.service.TeamService;
import grn.error.ConsoleHandler;

import java.io.File;
import java.sql.Timestamp;
import java.util.*;

public class TeamRepository implements Repository {
    private Map<Long, Team> teams = new HashMap<>();
    private int screensCount = 0;
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
        screensCount = calcScreenCount();
    }

    private int calcScreenCount() {
        int maxScreens = 0;
        for (Team team : teams.values()) {
            List<String> screens = team.getScreens();
            int teamScreens = screens.size();
            if (teamScreens > maxScreens)
                maxScreens = teamScreens;
        }
        return maxScreens;
    }

    public int getScreensCount() {
        return screensCount;
    }

    public void savePlayerHistory () {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        for (Team team : teams.values()) {
            TeamHistory teamHistory = new TeamHistory();
            teamHistory.setTeamId(team.getId());
            teamHistory.setScreenTime(now);
            int wins = 0;
            int loses = 0;
            List<Player> players = team.getPlayers();
            for (int i=0; i<players.size(); i++) {
                Player player = players.get(i);
                List<PlayerStats> playerStats = PlayerService.getLeagues(player);
                wins += getWins(playerStats);
                loses += getLoses(playerStats);
            }
            teamHistory.setWins(wins);
            teamHistory.setLoses(loses);
            teamHistory.setMatches(wins + loses);
            teamHistory.setLp(team.getTeamTierValue());
            TeamService.saveHistory(teamHistory);
        }
    }

    private int getWins (List<PlayerStats> playerStats) {
        int wins = 0;
        for (PlayerStats playerStat : playerStats)
            wins += playerStat.getWins();
        return wins;
    }

    private int getLoses (List<PlayerStats> playerStats) {
        int wins = 0;
        for (PlayerStats playerStat : playerStats)
            wins += playerStat.getLoses();
        return wins;
    }

    public List<Team> getAllTeams () {
        List<Team> allTeams = new ArrayList<>();
        List<Team> inactive = new ArrayList<>();
        for (Team team : teams.values()) {
            if (team.isActive())
                allTeams.add(team);
            else
                inactive.add(team);
        }
        allTeams.addAll(inactive);
        return allTeams;
    }

    public List<Team> getAllActiveTeams () {
        List<Team> allTeams = new ArrayList<>();
        for (Team team : teams.values()) {
            if (team.isActive())
                allTeams.add(team);
        }
        return allTeams;
    }

    public int getAverageTeamTier () {
        int sum = 0;
        int count = 0;
        for (Team team : teams.values()) {
            if (team.isActive()) {
                sum += team.getTeamTierValue();
                count++;
            }
        }
        return sum/count;
    }

    public List<Team> getAboveAverageTeams () {
        int averageTier = getAverageTeamTier();
        List<Team> filtered = new ArrayList<>();
        for (Team team : teams.values())
            if (team.getTeamTierValue() >= averageTier)
                filtered.add(team);
        return filtered;
    }

    public List<Team> getBelowAverageTeams () {
        int averageTier = getAverageTeamTier();
        List<Team> filtered = new ArrayList<>();
        for (Team team : teams.values())
            if (team.getTeamTierValue() <= averageTier)
                filtered.add(team);
        return filtered;
    }

    public List<Team> getUncategorizedTeams() {
        List<Team> higherTierTeams = getHigherTierTeams();
        List<Team> lowerTierTeams = getLowerTierTeams();
        List<Team> uncategorizedTeams = new ArrayList<>();
        for (Team team : teams.values()) {
            boolean higher = higherTierTeams.contains(team);
            boolean lower = lowerTierTeams.contains(team);
            if (!higher && !lower)
                uncategorizedTeams.add(team);
        }
        return uncategorizedTeams;
    }

    public List<Team> getHigherTierTeams () {
        Team bestTeam = getBestTeam();
        int highestTierValue = bestTeam.getTeamTierValue();
        int margin = (int)(highestTierValue * 0.33);
        return getTeamsWithTierMargin(highestTierValue, margin, true);
    }

    public List<Team> getLowerTierTeams () {
        Team worstTeam = getWorstTeam();
        int worstTierValue = worstTeam.getTeamTierValue();
        int margin = (int)(worstTierValue * 0.33);
        return getTeamsWithTierMargin(worstTierValue, margin, false);
    }

    public List<Team> getTeamsWithTierMargin(int tierValue, int margin, boolean higher) {
        List<Team> filtered = new ArrayList<>();
        for (Team team : teams.values()) {
            if (higher) {
                if (team.getTeamTierValue() >= (tierValue - margin))
                    filtered.add(team);
            } else {
                if (team.getTeamTierValue() <= (tierValue + margin))
                    filtered.add(team);
            }
        }
        return filtered;
    }

    public Team getWorstTeam () {
        List<Team> teams = getAllTeams();
        Collections.sort(teams);
        return teams.get(0);
    }

    public Team getBestTeam () {
        List<Team> teams = getAllTeams();
        Collections.sort(teams);
        Collections.reverse(teams);
        return teams.get(0);
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
