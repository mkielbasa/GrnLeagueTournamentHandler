package grn.riot.lol;

import com.example.grntournament.GrnTournamentApplication;
import grn.database.pojo.Player;
import grn.database.pojo.PlayerMatchStats;
import grn.database.repository.PlayerRepository;
import grn.database.service.MatchService;
import grn.error.ConsoleHandler;
import grn.riot.lol.endpoint.MatchEndpoint;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MatchController {

    public static List<String> getMatchIds (Player player) {
        return MatchEndpoint.getMatchIds(player.getPUuid());
    }

    public static List<PlayerMatchStats> getPlayerMatchStats (String matchId, long matchInternalId) {
        List<PlayerMatchStats> stats = new ArrayList<>();
        PlayerRepository playerRepository = GrnTournamentApplication.getPlayerRepository();
        JSONObject jStats = MatchEndpoint.getMatchDetails(matchId);
        JSONObject jInfo = (JSONObject) jStats.get("info");
        JSONArray jParticipants = (JSONArray) jInfo.get("participants");
        for (Object jObject : jParticipants.toArray()) {
            JSONObject jParticipant = (JSONObject) jObject;
            String pUUID = (String) jParticipant.get("puuid");
            if (!playerRepository.containsPlayer(pUUID))
                continue;
            Player player = playerRepository.get(pUUID);
            PlayerMatchStats playerMatchStats = new PlayerMatchStats();
            playerMatchStats.fromJson(jParticipant);
            playerMatchStats.setPlayerId(player.getInternalId());
            playerMatchStats.setTeamId(player.getTeamId());
            playerMatchStats.setMatchId(matchInternalId);
            stats.add(playerMatchStats);
        }
        return stats;
    }

    public static void testRegisteringAllPlayerMatchStats () {
        PlayerRepository playerRepository = GrnTournamentApplication.getPlayerRepository();
        int players = 1;
        for (Player player : playerRepository.getAll()) {
            if (players == 3)
                break;
            ConsoleHandler.handleInfo("Getting " + player.getName() + " matches...");
            List<String> matchIds = getMatchIds(player);
            for (String matchId : matchIds) {
                ConsoleHandler.handleInfo("Match " + matchId);
                List<PlayerMatchStats> playerMatchStats = getPlayerMatchStats(matchId, -1);
                for (PlayerMatchStats playerMatchStat : playerMatchStats) {
                    ConsoleHandler.handleInfo("Adding match info " + matchId);
                    MatchService.addMatchStats(playerMatchStat);
                }
            }
            players++;
        }
    }
}
