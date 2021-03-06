package grn.twitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import grn.database.pojo.*;
import grn.database.repository.Repositories;
import grn.database.repository.TeamRepository;
import grn.database.repository.ViewerScoreRepository;
import grn.database.service.MatchService;
import grn.properties.PropertiesHandler;
import grn.database.repository.MatchRepository;

import java.util.*;

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
            handleUserBestScores(viewer,message);
            handleCurrentMatch(viewer, message);
            handleGetKeywords(viewer, message);
            handleTeam(viewer, message);
            handlePlayer(viewer, message);
        } else {
            ViewerScoreRepository viewerScoreRepository = Repositories.getViewerScoreRepository();
            viewerScoreRepository.maybeAppendScore(viewer, message);
        }
    }

    private static void handleCommandlist (String viewer, String message) {
        String messageLowerCase = message.toLowerCase(Locale.ROOT);
        messageLowerCase = messageLowerCase.trim();
        if (!messageLowerCase.startsWith("!komendy"))
            return;
        sendMessage("Dostępne komendy: !gracz <nick>, !kibic, !kibicetop, !konkurs, !mecz, !mecze, !tabela, !team <nazwa drużyny>");
    }


    private static void handleUserScore (String viewer, String message) {
        String messageLowerCase = message.toLowerCase(Locale.ROOT);
        messageLowerCase = messageLowerCase.trim();
        if (!messageLowerCase.startsWith("!kibic"))
            return;
        String response;
        ViewerScoreRepository viewerScoreRepository = Repositories.getViewerScoreRepository();
        response = viewerScoreRepository.getViewerScores(viewer);
        sendMessage(response);
    }

    private static void handleUserBestScores (String viewer, String message) {
        String messageLowerCase = message.toLowerCase(Locale.ROOT);
        messageLowerCase = messageLowerCase.trim();
        if (!messageLowerCase.startsWith("!kibicetop"))
            return;
        String response;
        ViewerScoreRepository viewerScoreRepository = Repositories.getViewerScoreRepository();
        response = viewerScoreRepository.getBestViewerScores();
        sendMessage(response);
    }

    private static void handleCurrentMatch (String viewer, String message) {
        String messageLowerCase = message.toLowerCase(Locale.ROOT);
        messageLowerCase = messageLowerCase.trim();
        if (!messageLowerCase.startsWith("!mecz"))
            return;
        sendMessage(getCurrentMatch());
    }

    private static boolean teamExists (List<MatchStats> matchStats, Team team) {
        for (MatchStats matchStat : matchStats)
            if (matchStat.getTeamId() == team.getId())
                return true;
        return false;
    }

    private static void handleGetKeywords (String viewer, String message) {
        String messageLowerCase = message.toLowerCase(Locale.ROOT);
        messageLowerCase = messageLowerCase.trim();
        if (!messageLowerCase.startsWith("!konkurs"))
            return;
        MatchRepository matchController = Repositories.getMatchRepository();
        Match currentMatch = matchController.getCurrentMatch();
        if (currentMatch == null)  {
            sendMessage("W tej chwili nie ma na co głosować (brak meczu).");
            return;
        }
        long teamA = currentMatch.getTeamA();
        long teamB = currentMatch.getTeamB();

        ViewerScoreRepository viewerScoreRepository = Repositories.getViewerScoreRepository();
        StringBuilder sb = new StringBuilder();
        sb.append("Obecne słowa kluczowe to:\n");
        sb.append(viewerScoreRepository.getKeywords(teamA));
        sb.append(" oraz ");
        sb.append(viewerScoreRepository.getKeywords(teamB));
        sb.append(". Wielkość liter nie ma znaczenia.");
        sendMessage(sb.toString());
    }

    private static void handleTeam (String viewer, String message) {
        String messageLowerCase = message.toLowerCase(Locale.ROOT);
        messageLowerCase = messageLowerCase.trim();
        if (!messageLowerCase.startsWith("!team"))
            return;
        String[] parts = message.split(" ");
        if (parts.length != 2) {
            sendMessage("Nieprawidłowa składnia. Użyj np. !team HAD lub !team Kamienioki");
            return;
        }
        String team = parts[1];
        TeamRepository teamRepository = Repositories.getTeamRepository();
        Team foundTeam = teamRepository.getTeamByWord(team);
        if (foundTeam == null) {
            sendMessage("Nie znaleziono drużyny " + team + "...");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Skład drużyny " + team + ": ");
            for (Player player : foundTeam.getPlayers())
                sb.append(player.getName() + " (" + player.getTier() + ");  ");
            sendMessage(sb.toString());
        }
    }

    private static void handlePlayer (String viewer, String message) {
        String messageLowerCase = message.toLowerCase(Locale.ROOT);
        messageLowerCase = messageLowerCase.trim();
        if (!messageLowerCase.startsWith("!gracz"))
            return;
        String[] parts = message.split(" ");
        if (parts.length != 2) {
            sendMessage("Nieprawidłowa składnia. Użyj np. !gracz N3q");
            return;
        }
        String player = parts[1];
        TeamRepository teamRepository = Repositories.getTeamRepository();
        Player foundPlayer = teamRepository.getPlayerByWord(player);
        if (foundPlayer == null) {
            sendMessage("Gracz " + foundPlayer + " nie bierze udział w turnieju...");
            return;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Info o graczu " + player + ": ");
            sb.append("Nick w lolu:" + foundPlayer.getName());
            sb.append(" Ranga:" + foundPlayer.getTier());
            sb.append(" Lvl:" + foundPlayer.getSummonerLevel());
            sb.append(" Ulubione postaci:");
            int i = 1;
            for (ChampionMastery championMastery : foundPlayer.getMasteries()) {
                if (i == 6)
                    break;
                sb.append(i + ". ");
                sb.append(championMastery.getChampion().getName());
                sb.append(" (maestria:" + championMastery.getChampionPoints());
                sb.append("[lvl:" + championMastery.getChampionLevel() + "])  ");
                i++;
            }

            sendMessage(sb.toString());
        }
    }

    private static String getCurrentMatch () {
        MatchRepository matchController = Repositories.getMatchRepository();
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
