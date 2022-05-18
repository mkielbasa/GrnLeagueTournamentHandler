package grn.database.repository;

import grn.database.pojo.Match;
import grn.database.pojo.Player;
import grn.database.pojo.Team;
import grn.database.pojo.ViewerScore;
import grn.database.service.ViewerScoreService;

import java.util.*;

public class ViewerScoreRepository implements Repository {

    private Map<String, Map<Long,ViewerScore>> scores = new HashMap<>();
    private Map<Long, Set<String>> keyAliases = new HashMap<>();

    public void init () {
        TeamRepository teamRepository = Repositories.getTeamRepository();
        for (Team team : teamRepository.getAllTeams()) {
            long teamId = team.getId();
            Set<String> aliases = new LinkedHashSet<>();
            String[] splittedName = team.getName().split(" ");
            for (String splitted : splittedName)
                aliases.add(splitted);
            aliases.add(team.getShortName());
            for (Player player : team.getPlayers()) {
                splittedName = player.getName().split(" ");
                for (String splitted : splittedName) {
                    String withoutNumber = splitted.replaceAll("\\d*$", "");
                    aliases.add(withoutNumber);
                }
            }
            keyAliases.put(teamId, aliases);
        }
    }

    public void reload () {
        scores.clear();
        List<ViewerScore> currentScores = ViewerScoreService.getAllScores();
        for (ViewerScore score : currentScores)
            updateScore(score);
    }

    public void updateScore(ViewerScore score) {
        String viewer = score.getViewer();
        long teamId = score.getTeamId();
        if (! scores.containsKey(viewer))
            scores.put(viewer, new HashMap<>());
        scores.get(viewer).put(teamId, score);
    }

    public ViewerScore getScore (String viewer, long teamId) {
        if (!scores.containsKey(viewer))
            return null;
        if (!scores.get(viewer).containsKey(teamId))
            return null;
        return scores.get(viewer).get(teamId);
    }

    public String getKeywords (long teamId) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = keyAliases.get(teamId).iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next());
            if (iterator.hasNext())
                sb.append(", ");
        }
        return sb.toString();
    }

    public String getBestViewerScores () {
        StringBuilder sb = new StringBuilder();
        TeamRepository teamRepository = Repositories.getTeamRepository();
        for (Team team : teamRepository.getAllTeams()) {
            sb.append("Kibice " + team.getShortName() + ": ");
            List<ViewerScore> teamViewerScores =  getViewerScoresForTeam(team.getId());
            if (teamViewerScores.isEmpty()) {
                sb.append("brak;  ");
                continue;
            }
            Collections.sort(teamViewerScores);
            Collections.reverse(teamViewerScores);

            for (int i = 0; i < teamViewerScores.size(); i++) {
                if (i == 3)
                    break;
                ViewerScore score = teamViewerScores.get(i);
                sb.append((i+1) + ". " + score.getViewer() + "=" + score.getScore() + ";  ");
            }
        }
        return sb.toString();
    }

    public List<ViewerScore> getViewerScoresForTeam (long teamId) {
        List<ViewerScore> viewerScores = new ArrayList<>();
        for (String viewer : scores.keySet()) {
            if  (!scores.get(viewer).containsKey(teamId))
                continue;
            ViewerScore viewerScore = scores.get(viewer).get(teamId);
            viewerScores.add(viewerScore);
        }
        return viewerScores;
    }

    public String getViewerScores (String viewer) {
        StringBuilder sb = new StringBuilder();
        Map<Long,ViewerScore> score = scores.get(viewer);
        if (score == null)
            return "Jeszcze nie kibicowałaś/kibicowałeś. " +
                    "Słowa kluczowe bieżącego meczu sprawdź za pomocą !konkurs. " +
                    "Kibicuj za ich pomocą na czacie! Słowa kluczowe zmieniają się co mecz.";
        TeamRepository teamRepository = Repositories.getTeamRepository();
        sb.append("Wyniki kibicowania dla " + viewer + ":\n");
        for (Team team : teamRepository.getAllTeams()) {
            ViewerScore vs = score.get(team.getId());
            if (vs == null)
                continue;
            sb.append(team.getShortName() + " -> " + vs.getScore() + "\n");
        }
        return sb.toString();
    }

    public void maybeAppendScore (String viewer, String message) {
        MatchRepository matchController = Repositories.getMatchRepository();
        Match currentMatch = matchController.getCurrentMatch();
        long teamId = getTeamIdFromMessage(message);
        if (teamId < 0)
            return;
//        if (currentMatch != null) {
//            if (teamId != currentMatch.getTeamA() && teamId != currentMatch.getTeamB())
//                return;
//        }
        ViewerScore viewerScore = new ViewerScore();
        viewerScore.setViewer(viewer);
        viewerScore.setTeamId(teamId);
        viewerScore.setScore(1);
        appendScore(viewerScore);
    }

    public long getTeamIdFromMessage (String msg) {
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

    public void appendScore (ViewerScore score) {
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

    public void saveScores () {
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

    public Map<Long, Set<String>> getKeyAliases() {
        return keyAliases;
    }

    private boolean containsScore (ViewerScore score, List<ViewerScore> scores) {
        for (ViewerScore vs : scores)
            if (vs.getViewer().equals(score.getViewer())
                    && vs.getTeamId() == score.getTeamId())
                return true;
        return false;
    }
}
