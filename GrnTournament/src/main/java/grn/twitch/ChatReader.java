package grn.twitch;

import com.example.grntournament.GrnTournamentApplication;
import grn.database.pojo.Team;
import grn.database.repository.TeamRepository;
import grn.database.repository.ViewerScoreRepository;
import grn.error.ConsoleHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatReader {

    public static List<String> read(File file) {
        List<String> messages = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null)
                messages.add(line);
        } catch (IOException e) {
            ConsoleHandler.handleException(e);
        }
        return messages;
    }

    public static void parse () {
        List<String> messages = read(new File("./GrnTournament/chat_log.txt"));
        String dateTimeFormat = "\\[\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d\\]";
        Map<String, List<String>> viewerMessages = new HashMap<>();
        for (String message : messages) {
            String formattedMessage = message.replaceAll(dateTimeFormat, "");
            formattedMessage = formattedMessage.trim();
            if (!formattedMessage.startsWith("<"))
                continue;
            String[] parts = formattedMessage.split(" ");
            String viewer = parts[0];
            viewer = removeUnusedChars(viewer);
            String messagePart = concatenateMessage(parts);
            if (!viewerMessages.containsKey(viewer))
                viewerMessages.put(viewer, new ArrayList<>());
            viewerMessages.get(viewer).add(messagePart);
        }

        ConsoleHandler.handleInfo("===All messages===");
        for (String viewer : viewerMessages.keySet()) {
            ConsoleHandler.handleInfo("Viewer: " + viewer + " messages: " + viewerMessages.get(viewer).size());
            for (String message : viewerMessages.get(viewer)) {
                ConsoleHandler.handleInfo(" -> " + message);
            }
        }

        for (String viewer : viewerMessages.keySet()) {
            for (String message : viewerMessages.get(viewer)) {
                ViewerScoreRepository.maybeAppendScore(viewer, message);
            }
        }

        ConsoleHandler.handleInfo("===Best Scores===");
        ConsoleHandler.handleInfo(ViewerScoreRepository.getBestViewerScores());

        ConsoleHandler.handleInfo("===Individual Scores===");
        for (String viewer : viewerMessages.keySet()) {
            ConsoleHandler.handleInfo("Viewer: " + viewer);
            ConsoleHandler.handleInfo(ViewerScoreRepository.getViewerScores(viewer));
        }

        ConsoleHandler.handleInfo("===Used aliases===");
        TeamRepository teamRepository = GrnTournamentApplication.getTeamRepository();
        for (Team team : teamRepository.getAllTeams()) {
            ConsoleHandler.handleInfo("Team: " + team.getShortName());
            for (String alias : ViewerScoreRepository.getKeyAliases().get(team.getId())){
                System.out.println(" -> " + alias);
            }
        }
    }

    private static String concatenateMessage (String[] parts) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            sb.append(part + " ");
        }
        return sb.toString();
    }

    private static String removeUnusedChars (String message) {
        String[] formats = {"<", ">", "\\+", "\\~", "%", "\\^", "@"};
        for (String format : formats)
            message = message.replaceAll(format, "");
        return message;
    }

}
