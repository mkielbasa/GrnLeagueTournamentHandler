package grn.database.repository;

import grn.database.pojo.*;
import grn.database.service.PlayerService;
import grn.error.ConsoleHandler;
import grn.file.PlayerReader;
import grn.riot.lol.endpoint.ChampionMasteryEndpoint;
import grn.riot.lol.endpoint.LeagueEndpoint;
import grn.riot.lol.endpoint.SummonerEndpoint;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerRepository {
    private Map<Long, Player> players = new HashMap<>();
    private static final File TEAMS_FILE = new File("./GrnTournament/players.conf");

    public PlayerRepository (TeamRepository teamRepository) {
        buildPlayerProfiles(teamRepository);
    }

    private void loadPlayerProfiles (TeamRepository teamRepository) {
        for (Player player : PlayerService.getAllPlayers()) {
            this.players.put(player.getInternalId(), player);
            Team team = teamRepository.getTeam(player.getTeamId());
            team.addPlayer(player);
            List<ChampionMastery> masteries = PlayerService.getMasteries(player.getInternalId());
            player.setMasteries(masteries);
            team.updateWinRatio ();
        }
    }

    private void buildPlayerProfiles (TeamRepository teamRepository) {
        ConsoleHandler.handleInfo("Building players repository");
        //Read players and their assigment to teams
        Map<String, String> playersAndTeams = PlayerReader.read(TEAMS_FILE);
        for (String summoner : playersAndTeams.keySet()) {
            Team team = getPlayerTeam(summoner, teamRepository, playersAndTeams);
            //Skip players that are not assigned to any team
            if (team == null)
                continue;
            buildPlayerProfile(summoner, team);
        }
        loadPlayerProfiles(teamRepository);
        buildPlayersBestStats();
    }

    private void buildPlayersBestStats () {
        for (Player player : players.values()) {
            List<PlayerStats> playerStats = PlayerService.getLeagues(player);
            if (playerStats.isEmpty()) {
                PlayerStats ps = new PlayerStats();
                ps.setPlayerId(player.getInternalId());
                ps.setRank("UNRANKED");
                playerStats.add(ps);
            }
            PlayerStats higherStats = playerStats.get(0);
            int highestRank = PlayerRank.getRankValue(higherStats.getTier());
            for (PlayerStats ps : playerStats) {
                String tier = ps.getTier();
                int rank = PlayerRank.getRankValue(tier);
                if (rank > highestRank)
                    higherStats = ps;
            }
            player.setPlayerStats(higherStats);
        }
    }

    private Team getPlayerTeam (String summonerName, TeamRepository teamRepository,
                                   Map<String, String> playersAndTeams) {
        String teamName = playersAndTeams.get(summonerName);
        return teamRepository.getTeam(teamName);
    }

    private Player buildPlayerProfile(String summoner, Team team) {
        JSONObject jSummoner = SummonerEndpoint.getSummonerByName(summoner);
        Player player = new Player();
        player.fromJson(jSummoner);
        player.setTeamId(team.getId());
        if (!PlayerService.playerRegistered(player.getPUuid()))
            PlayerService.register(player);
        return player;
    }

    public void updatePlayerMaestries () {
        for (Player player : players.values()) {
            PlayerService.clearMasteries(player);
            JSONArray jMaestries = ChampionMasteryEndpoint.getChampionMaestries(player.getId());
            for (Object jObject : jMaestries.toArray()) {
                JSONObject jMaestry = (JSONObject) jObject;
                ChampionMastery championMastery = new ChampionMastery();
                championMastery.fromJson(jMaestry);
                championMastery.setPlayerId(player.getInternalId());
                PlayerService.addMastery(championMastery);
            }
        }
    }

    public void updatePlayerLeagues() {
        for (Player player : players.values()) {
            PlayerService.clearLeagues(player);
            JSONArray jLeagues = LeagueEndpoint.getLeagueEntries(player.getId());
            for (Object jObject : jLeagues.toArray()) {
                JSONObject jLeague =  (JSONObject) jObject;
                PlayerStats playerStats = new PlayerStats();
                playerStats.fromJson(jLeague);
                playerStats.setPlayerId(player.getInternalId());
                PlayerService.addLeague(playerStats);
            }
        }
    }

    public List<Player> getAll () {
        return new ArrayList<>(players.values());
    }

    public Player get (long internalId) {
        for (Player player : players.values())
            if (player.getInternalId() == internalId)
                return player;
        return null;
    }

    public Player get (String pUUID) {
        for (Player player : players.values())
            if (player.getPUuid().equals(pUUID))
                return player;
        return null;
    }

    public boolean containsPlayer (String pUUID) {
        for (Player player : players.values())
            if (player.getPUuid().equals(pUUID))
                return true;
        return false;
    }
}
