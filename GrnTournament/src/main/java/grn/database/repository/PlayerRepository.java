package grn.database.repository;

import grn.database.pojo.ChampionMastery;
import grn.database.pojo.Player;
import grn.database.pojo.PlayerStats;
import grn.database.pojo.Team;
import grn.database.service.PlayerService;
import grn.file.PlayerReader;
import grn.riot.lol.endpoint.ChampionMasteryEndpoint;
import grn.riot.lol.endpoint.LeagueEndpoint;
import grn.riot.lol.endpoint.SummonerEndpoint;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerRepository {
    private Map<Long, Player> players = new HashMap<>();
    private static final File TEAMS_FILE = new File("./GrnTournament/players.conf");

    public PlayerRepository (TeamRepository teamRepository) {
        Map<String, String> playersAndTeams = PlayerReader.read(TEAMS_FILE);
        for (String summoner : playersAndTeams.keySet()) {
            String teamName = playersAndTeams.get(summoner);
            Team team = teamRepository.getTeam(teamName);
            if (team == null)
                continue;
            JSONObject jSummoner = SummonerEndpoint.getSummonerByName(summoner);
            Player player = new Player();
            player.fromJson(jSummoner);
            player.setTeamId(team.getId());
            if (!PlayerService.playerRegistered(player.getPUuid())) {
                PlayerService.register(player);
            }
        }
        List<Player> players = PlayerService.getAllPlayers();
        for (Player player : players)
            this.players.put(player.getInternalId(), player);
        for (Player player : players) {
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
        for (Player player : players) {
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


}
