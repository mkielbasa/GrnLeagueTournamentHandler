package grn.riot.lol;

import com.example.grntournament.GrnTournamentApplication;
import grn.database.QueryRow;
import grn.database.pojo.Match;
import grn.database.pojo.Player;
import grn.database.pojo.PlayerMatchStats;
import grn.database.pojo.Team;
import grn.database.repository.PlayerRepository;
import grn.database.repository.TeamRepository;
import grn.database.service.MatchService;
import grn.error.ConsoleHandler;
import grn.riot.lol.endpoint.MatchEndpoint;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MatchController {

    public Match currentMatch;

    public void init () {
        reloadMatches();
    }

    private void reloadMatches () {
        List<QueryRow> rows = MatchService.getNonFinishedMatches();
        List<Match> nonFinishedMatches = new ArrayList<>();
        for (QueryRow row : rows) {
            Match match = new Match();
            match.fromQueryRow(row);
            nonFinishedMatches.add(match);
        }
        if (nonFinishedMatches.isEmpty()) {
            ConsoleHandler.handleWarning("Non finished matches is empty. Tournament is done");
            currentMatch = null;
            return;
        }
        currentMatch = nonFinishedMatches.get(0);;
        TeamRepository teamRepository = GrnTournamentApplication.getTeamRepository();
        Team teamA = teamRepository.getTeam(currentMatch.getTeamA());
        Team teamB = teamRepository.getTeam(currentMatch.getTeamB());
        ConsoleHandler.handleInfo("Current match : " + teamA.getShortName() + " vs " + teamB.getShortName());
    }

    public void finishCurrentMatch () {
        if (currentMatch == null || currentMatch.isFinished()) {
            ConsoleHandler.handleWarning("Current match is wrong");
            return;
        }
        TeamRepository teamRepository = GrnTournamentApplication.getTeamRepository();
        Team teamA = teamRepository.getTeam(currentMatch.getTeamA());
        Team teamB = teamRepository.getTeam(currentMatch.getTeamB());
        String tournamentMatchId = findTournamentMatchId(teamA, teamB);
        if (tournamentMatchId == null) {
            ConsoleHandler.handleWarning("Tournament match not found " +
                    teamA.getShortName() + " vs " + teamB.getShortName());
            reloadMatches();
            return;
        }
        registerMatchStats(tournamentMatchId, currentMatch.getId());
        reloadMatches();
    }

    private String findTournamentMatchId (Team teamA, Team teamB) {
        for (Player p : teamA.getPlayers()) {
            List<String> matchIds = MatchEndpoint.getMatchIds(p.getPUuid());
            if (matchIds.isEmpty())
                continue;
            String lastMatchId = matchIds.get(0);
            if (isTournamentMatch(teamA, teamB, lastMatchId))
                return lastMatchId;
        }
        return null;
    }

    private boolean isTournamentMatch (Team teamA, Team teamB, String matchId) {
        JSONObject jStats = MatchEndpoint.getMatchDetails(matchId);
        JSONObject jInfo = (JSONObject) jStats.get("info");
        JSONArray jParticipants = (JSONArray) jInfo.get("participants");
        int participantCount = 0;
        for (Object jObject : jParticipants.toArray()) {
            JSONObject jParticipant = (JSONObject) jObject;
            String pUUID = (String) jParticipant.get("puuid");
            if (teamA.containsPlayer(pUUID) || teamB.containsPlayer(pUUID))
                participantCount++;
        }
        return participantCount == currentMatch.getParticipants();
    }

    public Match getCurrentMatch() {
        return currentMatch;
    }

    public List<String> getMatchIds (Player player) {
        return MatchEndpoint.getMatchIds(player.getPUuid());
    }

    public List<PlayerMatchStats> getPlayerMatchStats (String matchId, long matchInternalId) {
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

    private void registerMatchStats (String matchId, long matchInternalId) {
        ConsoleHandler.handleInfo("Match " + matchId);
        List<PlayerMatchStats> playerMatchStats = getPlayerMatchStats(matchId, matchInternalId);
        for (PlayerMatchStats playerMatchStat : playerMatchStats) {
            ConsoleHandler.handleInfo("Adding match info " + matchId);
            MatchService.addMatchStats(playerMatchStat);
        }
        MatchService.finishMatch(matchInternalId, matchId);
    }

    public void testRegisteringAllPlayerMatchStats () {
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
