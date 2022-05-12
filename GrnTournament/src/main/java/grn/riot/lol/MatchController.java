package grn.riot.lol;

import com.example.grntournament.GrnTournamentApplication;
import grn.database.QueryRow;
import grn.database.pojo.Match;
import grn.database.pojo.Player;
import grn.database.pojo.PlayerMatchStats;
import grn.database.pojo.Team;
import grn.database.repository.PlayerRepository;
import grn.database.repository.Repositories;
import grn.database.repository.Repository;
import grn.database.repository.TeamRepository;
import grn.database.service.MatchService;
import grn.error.ConsoleHandler;
import grn.properties.PropertiesHandler;
import grn.properties.json.JsonFileReader;
import grn.riot.lol.endpoint.MatchEndpoint;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MatchController implements Repository {

    public Match currentMatch;

    public void init () {
        reloadMatches();
    }

    @Override
    public void reload() {
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
        TeamRepository teamRepository = Repositories.getTeamRepository();
        Team teamA = teamRepository.getTeam(currentMatch.getTeamA());
        Team teamB = teamRepository.getTeam(currentMatch.getTeamB());
        ConsoleHandler.handleInfo("Current match : " + teamA.getShortName() + " vs " + teamB.getShortName());
    }

    public void finishCurrentMatch () {
        if (currentMatch == null || currentMatch.isFinished()) {
            ConsoleHandler.handleWarning("Current match is wrong");
            return;
        }
        TeamRepository teamRepository = Repositories.getTeamRepository();
        Team teamA = teamRepository.getTeam(currentMatch.getTeamA());
        Team teamB = teamRepository.getTeam(currentMatch.getTeamB());
        String tournamentMatchId = findTournamentMatchId(teamA, teamB);
        if (tournamentMatchId == null) {
            ConsoleHandler.handleWarning("Tournament match not found " +
                    teamA.getShortName() + " vs " + teamB.getShortName());
            tournamentMatchId = "NO-ID";
            if (PropertiesHandler.instance().isDebug())
                registerMockMatchStats(tournamentMatchId, currentMatch.getId());
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

    public List<Match> getAllMatches () {
        List<QueryRow> rows = MatchService.getAllMatches();
        List<Match> matches = new ArrayList<>();
        for (QueryRow row : rows) {
            Match match = new Match();
            match.fromQueryRow(row);
            matches.add(match);
        }
        return matches;
    }

    public Match getCurrentMatch() {
        return currentMatch;
    }

    public List<String> getMatchIds (Player player) {
        return MatchEndpoint.getMatchIds(player.getPUuid());
    }

    public List<PlayerMatchStats> getMockPlayerMatchStats (long matchInternalId) {
        File file = new File(PropertiesHandler.instance().getMockMatch());
        JSONObject jStats = JsonFileReader.read(file);
        return buildPlayerMatchStats(jStats, matchInternalId);
    }

    public List<PlayerMatchStats> getPlayerMatchStats (String matchId, long matchInternalId) {
        JSONObject jStats = MatchEndpoint.getMatchDetails(matchId);
        return buildPlayerMatchStats(jStats, matchInternalId);
    }

    private List<PlayerMatchStats> buildPlayerMatchStats (JSONObject jStats, long matchInternalId) {
        List<PlayerMatchStats> stats = new ArrayList<>();
        PlayerRepository playerRepository = Repositories.getPlayerRepository();
        JSONObject jInfo = (JSONObject) jStats.get("info");
        long matchDuration = (long) jInfo.get("gameDuration");
        JSONArray jParticipants = (JSONArray) jInfo.get("participants");
        for (Object jObject : jParticipants.toArray()) {
            JSONObject jParticipant = (JSONObject) jObject;
            String summonerName = (String) jParticipant.get("summonerName");
            ConsoleHandler.handleInfo("Player: " + summonerName);
            String pUUID = (String) jParticipant.get("puuid");
            if (!playerRepository.containsPlayer(pUUID))
                continue;
            Player player = playerRepository.get(pUUID);
            PlayerMatchStats playerMatchStats = new PlayerMatchStats();
            playerMatchStats.fromJson(jParticipant);
            playerMatchStats.setPlayerId(player.getInternalId());
            playerMatchStats.setTeamId(player.getTeamId());
            playerMatchStats.setMatchId(matchInternalId);
            playerMatchStats.setMatchDuration(matchDuration);
            stats.add(playerMatchStats);
        }
        return stats;
    }

    private void registerMockMatchStats (String matchId, long matchInternalId) {
        ConsoleHandler.handleInfo("Match " + matchId);
        List<PlayerMatchStats> playerMatchStats = getMockPlayerMatchStats(matchInternalId);
        for (PlayerMatchStats playerMatchStat : playerMatchStats) {
            ConsoleHandler.handleInfo("Adding match info " + matchId);
            MatchService.addMatchStats(playerMatchStat);
        }
        MatchService.finishMatch(matchInternalId, matchId);
    }

    private void registerMatchStats (String matchId, long matchInternalId) {
        ConsoleHandler.handleInfo("Match " + matchId);
        if (!matchId.equals("NO-ID")) {
            List<PlayerMatchStats> playerMatchStats = getPlayerMatchStats(matchId, matchInternalId);
            for (PlayerMatchStats playerMatchStat : playerMatchStats) {
                ConsoleHandler.handleInfo("Adding match info " + matchId);
                MatchService.addMatchStats(playerMatchStat);
            }
        }
        MatchService.finishMatch(matchInternalId, matchId);
    }

}
