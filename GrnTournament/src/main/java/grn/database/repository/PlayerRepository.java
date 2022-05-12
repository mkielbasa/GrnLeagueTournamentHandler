package grn.database.repository;

import grn.database.pojo.*;
import grn.database.service.PlayerService;
import grn.endpoint.RequestResult;
import grn.error.ConsoleHandler;
import grn.exception.OutdatedApiKeyException;
import grn.file.PlayerReader;
import grn.endpoint.ChampionMasteryEndpoint;
import grn.endpoint.LeagueEndpoint;
import grn.endpoint.SummonerEndpoint;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerRepository implements Repository {
    private Map<Long, Player> players = new HashMap<>();
    private static final File TEAMS_FILE = new File("./GrnTournament/players.conf");

    private TeamRepository teamRepository;

    public PlayerRepository (TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public void init() throws OutdatedApiKeyException {
        reloadPlayerProfiles();
    }

    @Override
    public void reload() throws OutdatedApiKeyException {
        reloadPlayerProfiles();
    }

    private void reloadPlayerProfiles() throws OutdatedApiKeyException {
        ConsoleHandler.handleInfo("Building players repository");
        players.clear();
        //Read players and their assigment to teams
        Map<String, String> playersAndTeams = PlayerReader.read(TEAMS_FILE);
        for (String summoner : playersAndTeams.keySet()) {
            Team team = getPlayerTeam(summoner, teamRepository, playersAndTeams);
            //Skip players that are not assigned to any team
            if (team == null)
                continue;
            initPlayer(summoner, team);
        }
        initPlayerStats();
        buildPlayersBestStats();
    }

    private void initPlayerStats() {
        for (Player player : PlayerService.getAllPlayers()) {
            this.players.put(player.getInternalId(), player);
            Team team = teamRepository.getTeam(player.getTeamId());
            team.addPlayer(player);
            List<ChampionMastery> masteries = PlayerService.getMasteries(player.getInternalId());
            player.setMasteries(masteries);
            team.updateWinRatio ();
        }
    }

    private void buildPlayersBestStats () {
        for (Player player : players.values()) {
            List<PlayerStats> playerStats = PlayerService.getLeagues(player);
            if (playerStats.isEmpty()) {
                long playerId = player.getInternalId();
                playerStats.add(buildUnranked(playerId));
            }
            PlayerStats bestRank = findHighestRank(playerStats);
            player.setPlayerStats(bestRank);
        }
    }

    private PlayerStats buildUnranked (long playerId) {
        PlayerStats stats = new PlayerStats();
        stats.setPlayerId(playerId);
        stats.setRank("UNRANKED");
        return stats;
    }

    private PlayerStats findHighestRank (List<PlayerStats> playerStats) {
        PlayerStats bestRank = playerStats.get(0);
        int highestRank = PlayerRank.getRankValue(bestRank.getTier());
        for (PlayerStats ps : playerStats) {
            String tier = ps.getTier();
            int rank = PlayerRank.getRankValue(tier);
            if (rank > highestRank)
                bestRank = ps;
        }
        return bestRank;
    }

    private Team getPlayerTeam (String summonerName, TeamRepository teamRepository,
                                   Map<String, String> playersAndTeams) {
        String teamName = playersAndTeams.get(summonerName);
        return teamRepository.getTeam(teamName);
    }

    private Player initPlayer (String summoner, Team team) throws OutdatedApiKeyException {
        SummonerEndpoint sEndpoint = new SummonerEndpoint(summoner);
        RequestResult result = sEndpoint.doRequest();
        JSONObject jSummoner = (JSONObject) result.parseJSON();
        Player player = new Player();
        player.fromJson(jSummoner);
        player.setTeamId(team.getId());
        if (!PlayerService.playerRegistered(player.getPUuid()))
            PlayerService.register(player);
        return player;
    }

    public void initMaestries() throws OutdatedApiKeyException {
        for (Player player : players.values()) {
            PlayerService.clearMasteries(player);
            String summonerId = player.getSummonerId();
            ChampionMasteryEndpoint cEndpoint = new ChampionMasteryEndpoint(summonerId);
            RequestResult result = cEndpoint.doRequest();
            JSONArray jMaestries = (JSONArray) result.parseJSON();
            for (Object jObject : jMaestries.toArray()) {
                JSONObject jMaestry = (JSONObject) jObject;
                ChampionMastery championMastery = new ChampionMastery(jMaestry, player);
                PlayerService.addMastery(championMastery);
            }
        }
    }

    public void initLeagues() throws OutdatedApiKeyException {
        for (Player player : players.values()) {
            PlayerService.clearLeagues(player);
            LeagueEndpoint lEndpoint = new LeagueEndpoint(player.getSummonerId());
            RequestResult result = lEndpoint.doRequest();
            JSONArray jLeagues = (JSONArray) result.parseJSON();
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
