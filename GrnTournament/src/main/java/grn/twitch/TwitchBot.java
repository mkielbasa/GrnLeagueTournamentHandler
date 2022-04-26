package grn.twitch;

import com.example.grntournament.GrnTournamentApplication;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import grn.database.pojo.*;
import grn.database.repository.TeamRepository;
import grn.database.repository.ViewerScoreRepository;
import grn.database.service.MatchService;
import grn.properties.PropertiesHandler;
import grn.riot.lol.Champion;
import grn.riot.lol.MatchController;

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
            handleCurrentMatch(viewer, message);
            handleGetKeywords(viewer, message);
            handleTable(viewer, message);
            handleMatchQueue(viewer, message);
            handleTeam(viewer, message);
            handlePlayer(viewer, message);
        } else {
            ViewerScoreRepository.maybeAppendScore(viewer, message);
        }
    }

    private static void handleCommandlist (String viewer, String message) {
        String messageLowerCase = message.toLowerCase(Locale.ROOT);
        messageLowerCase = messageLowerCase.trim();
        if (!messageLowerCase.startsWith("!komendy"))
            return;
        sendMessage("Dostępne komendy: !gracz <nick>, !kibic, !konkurs, !mecz, !mecze, !tabela, !team <nazwa drużyny>");
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

    private static void handleTable (String viewer, String message) {
        String messageLowerCase = message.toLowerCase(Locale.ROOT);
        messageLowerCase = messageLowerCase.trim();
        if (!messageLowerCase.startsWith("!tabela"))
            return;
        List<MatchStats> matchStats = MatchService.getMatchStats();
        TeamRepository teamRepository = GrnTournamentApplication.getTeamRepository();
        for (MatchStats matchStat : matchStats) {
            Team team = teamRepository.getTeam(matchStat.getTeamId());
            Player player = team.getPlayers().get(0);
            long matchesDuration = MatchService.getMatchesDuration(player.getInternalId());
            long minutes = matchesDuration / 60;
            matchStat.setGoldForMinute(matchStat.getGoldEarned()/minutes);
            matchStat.setTeamName(team.getShortName());
            matchStat.setTeamIcon(team.getIcon());
        }

        for (Team team : teamRepository.getAllTeams()) {
            if (!teamExists(matchStats, team)) {
                MatchStats matchStat = new MatchStats();
                matchStat.setTeamId(team.getId());
                matchStat.setTeamName(team.getShortName());
                matchStat.setTeamIcon(team.getIcon());
                matchStats.add(matchStat);
            }
        }
        Collections.sort(matchStats);
        Collections.reverse(matchStats);

        StringBuilder sb = new StringBuilder();
        sb.append("Bieżąca tabela: ");
        int i = 1;
        for (MatchStats matchStat : matchStats) {
            sb.append(i + ". ");
            sb.append(matchStat.getTeamName() + " Pkt: " + matchStat.getWins() + " Gold/Min:" + matchStat.getGoldForMinute());
            sb.append(";  ");
            i++;
        }

        sendMessage(sb.toString());
    }

    private static void handleMatchQueue (String viewer, String message) {
        String messageLowerCase = message.toLowerCase(Locale.ROOT);
        messageLowerCase = messageLowerCase.trim();
        if (!messageLowerCase.startsWith("!mecze"))
            return;
        MatchController matchController = GrnTournamentApplication.getMatchController();
        List<Match> matches = matchController.getAllMatches();
        StringBuilder sb = new StringBuilder();
        sb.append("Szacowane godziny i kolejność meczy: ");
        for (Match match : matches) {
            sb.append(match.getTime() + ": " +
                    match.getTeamAShortName() + " " + match.getResult() + " " + match.getTeamBShortName());
            sb.append(";   ");
        }
        sendMessage(sb.toString());
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
        TeamRepository teamRepository = GrnTournamentApplication.getTeamRepository();
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
        TeamRepository teamRepository = GrnTournamentApplication.getTeamRepository();
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
