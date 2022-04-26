package grn.twitch;

import com.example.grntournament.GrnTournamentApplication;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import grn.database.pojo.Match;
import grn.database.repository.ViewerScoreRepository;
import grn.properties.PropertiesHandler;
import grn.riot.lol.MatchController;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class TwitchBot {

    private static TwitchClient twitchClient;
    private static Set<String> viewerBlacklist = new HashSet<>();

    public static void init () {
        PropertiesHandler ph = PropertiesHandler.instance();
        OAuth2Credential credential = new OAuth2Credential("twitch", ph.getTwitchOAuth());
        twitchClient = TwitchClientBuilder.builder().withEnableHelix(true).withEnableChat(true)
                .withChatAccount(credential).build();
        twitchClient.getChat().joinChannel(ph.getTwitchChannel());
        twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, e -> handleIncomingMessage(e));
        sendMessage("GrnLeague bot właśnie został aktywowany!");
        sendMessage(getCurrentMatch());
    }

    private static void initBlacklist () {
        if (!PropertiesHandler.instance().isDebug())
            viewerBlacklist.add("66FF00");
    }

    public static void sendMessage (String message) {
        PropertiesHandler ph = PropertiesHandler.instance();
        twitchClient.getChat().sendMessage(ph.getTwitchChannel(), message);
    }

    private static void handleIncomingMessage(ChannelMessageEvent event) {
        String viewer = event.getUser().getName();
        if (isBlacklisted(viewer))
            return;
        String message = event.getMessage();
        if (isCommand(message)) {
            handleCommandlist(viewer, message);
            handleUserScore(viewer, message);
            handleCurrentMatch(viewer, message);
            handleGetKeywords(viewer, message);
        } else {
            ViewerScoreRepository.maybeAppendScore(viewer, message);
        }
    }

    private static void handleCommandlist (String viewer, String message) {
        String messageLowerCase = message.toLowerCase(Locale.ROOT);
        messageLowerCase = messageLowerCase.trim();
        if (!messageLowerCase.startsWith("!komendy"))
            return;
        sendMessage("Dostępne komendy: !kibic, !mecz, !konkurs");
    }


    private static void handleUserScore (String viewer, String message) {
        String messageLowerCase = message.toLowerCase(Locale.ROOT);
        messageLowerCase = messageLowerCase.trim();
        if (!messageLowerCase.startsWith("!kibic"))
            return;
        String response;
        response = ViewerScoreRepository.getViewerScores(viewer);
        sendMessage(response);
    }

    private static void handleCurrentMatch (String viewer, String message) {
        String messageLowerCase = message.toLowerCase(Locale.ROOT);
        messageLowerCase = messageLowerCase.trim();
        if (!messageLowerCase.startsWith("!mecz"))
            return;
        sendMessage(getCurrentMatch());
    }

    private static void handleGetKeywords (String viewer, String message) {
        String messageLowerCase = message.toLowerCase(Locale.ROOT);
        messageLowerCase = messageLowerCase.trim();
        if (!messageLowerCase.startsWith("!konkurs"))
            return;
        MatchController matchController = GrnTournamentApplication.getMatchController();
        Match currentMatch = matchController.getCurrentMatch();
        if (currentMatch == null)  {
            sendMessage("W tej chwili nie ma na co głosować (brak meczu).");
            return;
        }
        long teamA = currentMatch.getTeamA();
        long teamB = currentMatch.getTeamB();

        StringBuilder sb = new StringBuilder();
        sb.append("Obecne słowa kluczowe to:\n");
        sb.append(ViewerScoreRepository.getKeywords(teamA));
        sb.append(" oraz ");
        sb.append(ViewerScoreRepository.getKeywords(teamB));
        sb.append(". Wielkość liter nie ma znaczenia.");
        sendMessage(sb.toString());
    }

    private static String getCurrentMatch () {
        MatchController matchController = GrnTournamentApplication.getMatchController();
        Match currentMatch = matchController.getCurrentMatch();
        if (currentMatch == null) {
            return "W tej chwili nie będzie rozgrywany mecz.";
        } else {
            return "Bieżący mecz: " + currentMatch.getTeamAShortName() + " vs " + currentMatch.getTeamBShortName();
        }
    }

    private static boolean isBlacklisted (String viewer) {
        for (String blacklist : viewerBlacklist)
            if (blacklist.equalsIgnoreCase(viewer))
                return true;
        return false;
    }

    private static boolean isCommand (String msg) {
        return msg.startsWith("!");
    }
}
