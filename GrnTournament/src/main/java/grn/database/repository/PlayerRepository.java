package grn.database.repository;

import grn.database.pojo.*;
import grn.database.service.PlayerService;
import grn.endpoint.*;
import grn.error.ConsoleHandler;
import grn.exception.BadRequestException;
import grn.exception.EndpointException;
import grn.exception.NotFoundException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerRepository implements Repository {
    private Map<Long, Player> players = new HashMap<>();
    private static final File TEAMS_FILE = new File("./GrnTournament/players.conf");

    private TeamRepository teamRepository;
    private Player currentPlayer;

    public PlayerRepository (TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public void init() throws EndpointException {
        reloadPlayerProfiles();
    }

    @Override
    public void reload() throws EndpointException {
        reloadPlayerProfiles();
    }

    private void reloadPlayerProfiles() throws EndpointException {
        ConsoleHandler.handleInfo("Building players repository");
        players.clear();
        List<Player> allPlayers = PlayerService.getAllPlayers();
        for (Player player : allPlayers)
            players.put(player.getInternalId(), player);
        loadPlayerStats();
        loadPlayersBestStats();
        this.currentPlayer = players.values().iterator().next();
    }

    private void loadPlayerStats() {
        for (Player player : PlayerService.getAllPlayers()) {
            this.players.put(player.getInternalId(), player);
            Team team = teamRepository.getTeam(player.getTeamId());
            team.addPlayer(player);
            List<ChampionMastery> masteries = PlayerService.getMasteries(player.getInternalId());
            player.setMasteries(masteries);
            team.updateWinRatio ();
        }
    }

    public void savePlayerHistory () {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        for (Player player : players.values()) {
            List<PlayerStats> playerStats = PlayerService.getLeagues(player);
            PlayerHistory playerHistory = new PlayerHistory();
            playerHistory.setPlayerId(player.getInternalId());
            playerHistory.setLp(player.getTierValue());
            playerHistory.setScreenTime(now);
            int wins = getWins(playerStats);
            int loses = getLoses(playerStats);
            playerHistory.setWins(wins);
            playerHistory.setLoses(loses);
            playerHistory.setMatches(wins + loses);
            PlayerService.saveHistory(playerHistory);
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
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

    private void loadPlayersBestStats() {
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

    public void checkPlayers () throws EndpointException {
        for (Player player : players.values()) {
            try {
                SummonerEndpoint sEndpoint = new SummonerEndpoint(player.getName(), player.getServer());
                sEndpoint.doRequest();
            } catch (NotFoundException e) {
                ConsoleHandler.handleWarning("Player " + player.getName() + " not found (probably has changed name)");
                ConsoleHandler.handleWarning("Probably new name is: " + getProbablyNewName(player.getPUuid()));
            }
        }
    }

    public Player initPlayer (String summoner, Team team) throws EndpointException {
        try {
            SummonerEndpoint sEndpoint = new SummonerEndpoint(summoner, "eun1");
            RequestResult result = sEndpoint.doRequest();
            JSONObject jSummoner = (JSONObject) result.parseJSON();
            Player player = new Player();
            player.fromJson(jSummoner);
            player.setTeamId(team.getId());
            if (!PlayerService.playerRegistered(player.getPUuid()))
                PlayerService.register(player);
            return player;
        } catch (NotFoundException e) {
            ConsoleHandler.handleWarning("Player " + summoner + " not found (probably has changed name)");
            Player p = PlayerService.getPlayer(summoner);
            if (p==null)
                return null;
            ConsoleHandler.handleWarning("Probably new name is: " + getProbablyNewName(p.getPUuid()));
            return null;
        }
    }

    public void initMaestries() throws EndpointException {
        for (Player player : players.values()) {
            initMaestries(player);
        }
    }

    public void initMaestries(Player player) throws EndpointException {
        PlayerService.clearMasteries(player);
        String summonerId = player.getSummonerId();
        try {
            ChampionMasteryEndpoint cEndpoint = new ChampionMasteryEndpoint(summonerId, player.getServer());
            RequestResult result = cEndpoint.doRequest();
            JSONArray jMaestries = (JSONArray) result.parseJSON();
            for (Object jObject : jMaestries.toArray()) {
                JSONObject jMaestry = (JSONObject) jObject;
                ChampionMastery championMastery = new ChampionMastery(jMaestry, player);
                PlayerService.addMastery(championMastery);
            }
        } catch (BadRequestException e) {
            ConsoleHandler.handleWarning("Player " + player.getName() + "(" + player.getPUuid() + ") doesn't exists anymore!");
            ConsoleHandler.handleWarning("Probably new name is: " + getProbablyNewName(player.getPUuid()));
        }
    }

    private String getProbablyNewName (String puuid) throws EndpointException {
        SummonerUUIDEndpoint sEndpoint = new SummonerUUIDEndpoint(puuid, "eun1");
        RequestResult result = sEndpoint.doRequest();
        JSONObject jObject = (JSONObject) result.parseJSON();
        String gameName = (String) jObject.get("name");
        return gameName;
    }

    public void initLeagues() throws EndpointException {
        for (Player player : players.values()) {
            initLeagues(player);
        }
    }

    public void initLeagues(Player player) throws EndpointException {
        try {
            PlayerService.clearLeagues(player);
            LeagueEndpoint lEndpoint = new LeagueEndpoint(player.getSummonerId(), player.getServer());
                RequestResult result = lEndpoint.doRequest();
                JSONArray jLeagues = (JSONArray) result.parseJSON();
                for (Object jObject : jLeagues.toArray()) {
                    JSONObject jLeague =  (JSONObject) jObject;
                    PlayerStats playerStats = new PlayerStats();
                    playerStats.fromJson(jLeague);
                    playerStats.setPlayerId(player.getInternalId());
                    PlayerService.addLeague(playerStats);
                }
            } catch (BadRequestException e) {
                ConsoleHandler.handleWarning("Player " + player.getName() + "(" + player.getPUuid() + ") doesn't exists anymore!");
                ConsoleHandler.handleWarning("Probably new name is: " + getProbablyNewName(player.getPUuid()));
        }
    }

    public List<Player> getAll () {
        return new ArrayList<>(players.values());
    }

    public List<Player> getAllByTeams () {
        List<Player> inactive = new ArrayList<>();
        List<Player> playerList = new ArrayList<>();
        TeamRepository teamRepository = Repositories.getTeamRepository();
        for (Team team : teamRepository.getAllTeams()) {
            for (Player player : team.getPlayers()) {
                if (player.isActive())
                    playerList.add(player);
                else
                    inactive.add(player);
            }
        }
        playerList.addAll(inactive);
        return playerList;
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

    public void setCurrentPlayer(long internalId) {
        this.currentPlayer = players.get(internalId);
    }
}
