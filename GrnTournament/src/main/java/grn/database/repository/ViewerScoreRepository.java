package grn.database.repository;

import com.example.grntournament.GrnTournamentApplication;
import grn.database.pojo.Player;
import grn.database.pojo.Team;
import grn.database.pojo.ViewerScore;
import grn.database.service.ViewerScoreService;

import java.util.*;

public class ViewerScoreRepository {

    private static Map<String, Map<Long,ViewerScore>> scores = new HashMap<>();
    private static Map<Long, Set<String>> keyAliases = new HashMap<>();

    public static void init () {
        TeamRepository teamRepository = GrnTournamentApplication.getTeamRepository();
        for (Team team : teamRepository.getAllTeams()) {
            long teamId = team.getId();
            Set<String> aliases = new HashSet<>();
            aliases.add(team.getName());
            aliases.add(team.getShortName());
            for (Player player : team.getPlayers())
                aliases.add(player.getName());
            keyAliases.put(teamId, aliases);
        }
    }

    public static void reload () {
        scores.clear();
        List<ViewerScore> currentScores = ViewerScoreService.getAllScores();
        for (ViewerScore score : currentScores)
            updateScore(score);
    }

    public static void updateScore(ViewerScore score) {
        String viewer = score.getViewer();
        long teamId = score.getTeamId();
        if (! scores.containsKey(viewer))
            scores.put(viewer, new HashMap<>());
        scores.get(viewer).put(teamId, score);
    }

    public static ViewerScore getScore (String viewer, long teamId) {
        if (!scores.containsKey(viewer))
            return null;
        if (!scores.get(viewer).containsKey(teamId))
            return null;
        return scores.get(viewer).get(teamId);
    }

    public static String getKeywords (long teamId) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = keyAliases.get(teamId).iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next());
            if (iterator.hasNext())
                sb.append(", ");
        }
        return sb.toString();
    }

    public static String getViewerScores (String viewer) {
        StringBuilder sb = new StringBuilder();
        Map<Long,ViewerScore> score = scores.get(viewer);
        if (score == null)
            return "Ten użytkownik jeszcze nie kibicował. Głosuj za pomocą słów kluczowych aby mieć szanse wygrać nagrodę! " +
                    "Słowa kluczowe bieżącego meczu sprawdź za pomocą !konkurs .";
        TeamRepository teamRepository = GrnTournamentApplication.getTeamRepository();
        sb.append("Wyniki kibicowania dla " + viewer + ":\n");
        for (Team team : teamRepository.getAllTeams()) {
            ViewerScore vs = score.get(team.getId());
            if (vs == null)
                continue;
            sb.append(team.getShortName() + " -> " + vs.getScore() + "\n");
        }
        return sb.toString();
    }

    public static void maybeAppendScore (String viewer, String message) {
        long teamId = getTeamIdFromMessage(message);
        if (teamId < 0)
            return;
        ViewerScore viewerScore = new ViewerScore();
        viewerScore.setViewer(viewer);
        viewerScore.setTeamId(teamId);
        viewerScore.setScore(1);
        appendScore(viewerScore);
    }

    public static long getTeamIdFromMessage (String msg) {
        for (long teamId : keyAliases.keySet()) {
            for (String keyword : keyAliases.get(teamId)) {
                String lowerCaseMsg = msg.toLowerCase(Locale.ROOT);
                String lowerCaseKeyword = keyword.toLowerCase(Locale.ROOT);
                if (lowerCaseMsg.contains(lowerCaseKeyword))
                    return teamId;
            }
        }
        return -1;
    }

    public static void appendScore (ViewerScore score) {
        ViewerScore currentScore = getScore(score.getViewer(), score.getTeamId());
        if (currentScore == null) {
            currentScore = new ViewerScore();
            currentScore.setViewer(score.getViewer());
            currentScore.setTeamId(score.getTeamId());
            currentScore.setScore(score.getScore());
        } else {
            currentScore.setScore(currentScore.getScore() + score.getScore());
        }
        updateScore(currentScore);
    }

    public static void saveScores () {
        List<ViewerScore> currentScores = ViewerScoreService.getAllScores();
        for (String viewer : scores.keySet()) {
            Map<Long, ViewerScore> teamScores = scores.get(viewer);
            for (ViewerScore viewerScore : teamScores.values()) {
                if (containsScore(viewerScore, currentScores))
                    ViewerScoreService.update(viewerScore);
                else
                    ViewerScoreService.register(viewerScore);
            }
        }
    }

    private static boolean containsScore (ViewerScore score, List<ViewerScore> scores) {
        for (ViewerScore vs : scores)
            if (vs.getViewer().equals(score.getViewer())
                    && vs.getTeamId() == score.getTeamId())
                return true;
        return false;
    }
}
