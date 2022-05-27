package com.example.grntournament.controlloer;

import grn.database.pojo.*;
import grn.database.repository.PlayerRepository;
import grn.database.repository.Repositories;
import grn.database.repository.TeamRepository;
import grn.database.service.MatchService;
import grn.database.service.PlayerService;
import grn.database.service.TeamService;
import grn.exception.EndpointException;
import grn.database.repository.MatchRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class HttpController {

    @GetMapping("/index")
    public ModelAndView getIndex () {
        ModelAndView mav = new ModelAndView("index");
        return mav;
    }

    @GetMapping("/players")
    public ModelAndView getPlayers () {
        PlayerRepository playerRepository = Repositories.getPlayerRepository();
        List<Player> players = playerRepository.getAllByTeams();
        ModelAndView mav = new ModelAndView("players");
        mav.addObject("players", players);
        return mav;
    }

    @GetMapping("/editPlayer")
    public String getEditPlayer (@RequestParam String id, Model model) {
        PlayerRepository playerRepository = Repositories.getPlayerRepository();
        TeamRepository teamRepository = Repositories.getTeamRepository();
        long internalId = Long.parseLong(id);
        List<Team> teams = teamRepository.getAllTeams();
        Player player = playerRepository.get(internalId);
        model.addAttribute("player", player);
        model.addAttribute("teams", teams);
        return "playerEdit";
    }

    @PostMapping("/editPlayer")
    public String getEditPlayerSubmit(@ModelAttribute Player player, Model model) throws EndpointException {
        PlayerRepository playerRepository = Repositories.getPlayerRepository();
        TeamRepository teamRepository = Repositories.getTeamRepository();
        List<Team> teams = teamRepository.getAllTeams();
        model.addAttribute("player", player);
        model.addAttribute("teams", teams);
        PlayerService.update(player);
        teamRepository.reload();
        playerRepository.reload();
        return "playerEdit";
    }

    @GetMapping("/editTeam")
    public String getEditTeam (@RequestParam String id, Model model) {
        TeamRepository teamRepository = Repositories.getTeamRepository();
        long internalId = Long.parseLong(id);
        Team team = teamRepository.getTeam(internalId);
        model.addAttribute("team", team);
        return "teamEdit";
    }

    @PostMapping("/editTeam")
    public String getEditTeamSubmit (@ModelAttribute Team team, Model model) throws EndpointException {
        PlayerRepository playerRepository = Repositories.getPlayerRepository();
        TeamRepository teamRepository = Repositories.getTeamRepository();
        model.addAttribute("team", team);
        TeamService.update (team);
        teamRepository.reload();
        playerRepository.reload();
        return "teamEdit";
    }

    @GetMapping("/createTeam")
    public String getCreateTeam (Model model) {
        Team team = new Team();
        model.addAttribute("team", team);
        return "teamCreate";
    }

    @PostMapping("/createTeam")
    public String getCreateTeamSubmit (@ModelAttribute Team team, Model model) throws EndpointException {
        PlayerRepository playerRepository = Repositories.getPlayerRepository();
        TeamRepository teamRepository = Repositories.getTeamRepository();
        team.setIcon(team.getShortName()+".png");
        model.addAttribute("team", team);
        TeamService.register(team);
        teamRepository.reload();
        playerRepository.reload();
        return "teamCreate";
    }

    @GetMapping("/deleteTeam")
    public String getDeleteTeam (@RequestParam String id) throws EndpointException {
        PlayerRepository playerRepository = Repositories.getPlayerRepository();
        TeamRepository teamRepository = Repositories.getTeamRepository();
        long internalId = Long.parseLong(id);
        TeamService.unregister (internalId);
        teamRepository.reload();
        playerRepository.reload();
        return "index";
    }

    @GetMapping("/deletePlayer")
    public String getDeletePlayer (@RequestParam String id) throws EndpointException {
        PlayerRepository playerRepository = Repositories.getPlayerRepository();
        TeamRepository teamRepository = Repositories.getTeamRepository();
        long internalId = Long.parseLong(id);
        PlayerService.unregister (internalId);
        teamRepository.reload();
        playerRepository.reload();
        return "index";
    }
    @GetMapping("/createPlayer")
    public String getCreatePlayer (Model model) {
        TeamRepository teamRepository = Repositories.getTeamRepository();
        List<Team> teams = teamRepository.getAllTeams();
        Player player = new Player();
        model.addAttribute("player", player);
        model.addAttribute("teams", teams);
        return "playerCreate";
    }

    @PostMapping("/createPlayer")
    public String getCreatePlayerSubmit (@ModelAttribute Player player, Model model) throws EndpointException {
        PlayerRepository playerRepository = Repositories.getPlayerRepository();
        TeamRepository teamRepository = Repositories.getTeamRepository();
        List<Team> teams = teamRepository.getAllTeams();
        Team team = teamRepository.getTeam(player.getTeamId());
        model.addAttribute("player", player);
        model.addAttribute("teams", teams);
        playerRepository.initPlayer(player.getName(), team);
        teamRepository.reload();
        playerRepository.reload();
        return "playerCreate";
    }

    @GetMapping("/checkPlayers")
    public String getCheckPlayers () throws EndpointException {
        PlayerRepository playerRepository = Repositories.getPlayerRepository();
        playerRepository.checkPlayers();
        return "index";
    }

    @GetMapping("/teams")
    public ModelAndView getTeams () {
        TeamRepository teamRepository = Repositories.getTeamRepository();
        List<Team> teams = teamRepository.getAllTeams();
        List<Team> activeTeams = teamRepository.getAllActiveTeams();
        ModelAndView mav = new ModelAndView("teams");
        buildTeamModelAndView(mav, activeTeams, teams);
        return mav;
    }

    private void buildTeamModelAndView (ModelAndView mav, List<Team> activeTeams, List<Team> teams) {
        mav.addObject("teams", teams);
        mav.addObject("screens", activeTeams.get(0).getScreens());
        for (int i=0; i<activeTeams.size();i++) {
            Team team = activeTeams.get(i);
            String teamLabel = "team" + (i+1);
            String matchesLabel = teamLabel + "matches";
            String lpLabel = teamLabel + "lps";
            mav.addObject(teamLabel, team);
            mav.addObject(matchesLabel, team.getMatchesHistory());
            mav.addObject(lpLabel, team.getLpHistory());
        }
    }

    @GetMapping("/teamTiers")
    public ModelAndView getTeamsTiers () {
        TeamRepository teamRepository = Repositories.getTeamRepository();
        List<Team> higherTierTeams = teamRepository.getHigherTierTeams();
        List<Team> lowerTierTeams = teamRepository.getLowerTierTeams();
        List<Team> uncategorizedTeams = teamRepository.getUncategorizedTeams();
        ModelAndView mav = new ModelAndView("teamsTiers");
        mav.addObject("higherTierTeams", higherTierTeams);
        mav.addObject("lowerTierTeams", lowerTierTeams);
        mav.addObject("uncategorizedTeams", uncategorizedTeams);
        mav.addObject("averageTier", "Average tier: " +teamRepository.getAverageTeamTier());
        return mav;
    }
    @GetMapping("/teamTiersAverage")
    public ModelAndView getTeamsTiersAverage () {
        TeamRepository teamRepository = Repositories.getTeamRepository();
        List<Team> higherTierTeams = teamRepository.getAboveAverageTeams();
        List<Team> lowerTierTeams = teamRepository.getBelowAverageTeams();
        List<Team> uncategorizedTeams = new ArrayList<>();
        ModelAndView mav = new ModelAndView("teamsTiers");
        mav.addObject("higherTierTeams", higherTierTeams);
        mav.addObject("lowerTierTeams", lowerTierTeams);
        mav.addObject("uncategorizedTeams", uncategorizedTeams);
        mav.addObject("averageTier", "Average tier: " +teamRepository.getAverageTeamTier());
        return mav;
    }

    @GetMapping("/tree4")
    public ModelAndView getTree4 () {
        return buildTree8Model("tree8", 1);
    }

    @GetMapping("/tree4preview")
    public ModelAndView getTree4Preview () {
        return buildTree8Model("tree8preview", 1);
    }

    @GetMapping("/tree8")
    public ModelAndView getTree8 () {
        return buildTree8Model("tree8", 2);
    }

    @GetMapping("/tree8preview")
    public ModelAndView getTree8Preview () {
        return buildTree8Model("tree8preview", 2);
    }

    public ModelAndView buildTree8Model (String view, int tier) {
        MatchRepository matchRepository = Repositories.getMatchRepository();
        Match currentMatch = matchRepository.getCurrentMatch();
        Match finalMatch = matchRepository.getFinalMatch(tier);
        List<Match> semiFinals = matchRepository.getMatchesWithParent(tier, finalMatch);
        List<Match> starters1 = matchRepository.getMatchesWithParent(tier, semiFinals.get(0));
        List<Match> starters2 = matchRepository.getMatchesWithParent(tier, semiFinals.get(1));
        ModelAndView mav = new ModelAndView(view);
        mav.addObject("final", finalMatch);
        mav.addObject("semifinal1", semiFinals.get(0));
        mav.addObject("semifinal2", semiFinals.get(1));
        mav.addObject("starter1", starters1.get(0));
        mav.addObject("starter2", starters1.get(1));
        mav.addObject("starter3", starters2.get(0));
        mav.addObject("starter4", starters2.get(1));
        mav.addObject("currentMatch", currentMatch);
        return mav;
    }

    @GetMapping("/editMatch")
    public String getEditMatch (@RequestParam String id, Model model) {
        MatchRepository matchRepository = Repositories.getMatchRepository();
        TeamRepository teamRepository = Repositories.getTeamRepository();
        List<Team> teams = teamRepository.getAllTeams();
        long internalId = Long.parseLong(id);
        Match match = matchRepository.getMatch(internalId);
        model.addAttribute("match", match);
        model.addAttribute("teams", teams);
        return "matchEdit";
    }

    @PostMapping("/editMatch")
    public String getEditMatchSubmit (@ModelAttribute Match match, Model model) throws EndpointException {
        MatchRepository matchRepository = Repositories.getMatchRepository();
        TeamRepository teamRepository = Repositories.getTeamRepository();
        List<Team> teams = teamRepository.getAllTeams();
        model.addAttribute("match", match);
        model.addAttribute("teams", teams);
        MatchService.updateTeam(match.getId(), match.getTeamA(), "teama");
        MatchService.updateTeam(match.getId(), match.getTeamB(), "teamb");
        matchRepository.reload();
        return "matchEdit";
    }

    @GetMapping("/updateMatchScore")
    public String getUpdateMatchScore (@RequestParam String id,
                                       @RequestParam String scoreA, @RequestParam String scoreB) {
        MatchRepository matchRepository = Repositories.getMatchRepository();
        long internalId = Long.parseLong(id);
        int sA = Integer.parseInt(scoreA);
        int sB = Integer.parseInt(scoreB);
        MatchService.updateScore(internalId, sA, sB);
        Match match = matchRepository.getMatch(internalId);
        Match parentMatch = matchRepository.getMatch(match.getParent());
        if (sA == 3) {
            MatchService.finishMatch(internalId, match.getTeamAObject());
            if (parentMatch != null) {
                if (parentMatch.getTeamAObject() == null)
                    MatchService.updateTeam(parentMatch.getId(), match.getTeamA(), "teama");
                else if (parentMatch.getTeamBObject() == null)
                    MatchService.updateTeam(parentMatch.getId(), match.getTeamA(), "teamb");
            }
        } else if (sB == 3) {
            MatchService.finishMatch(internalId, match.getTeamBObject());
            if (parentMatch != null) {
                if (parentMatch.getTeamAObject() == null)
                    MatchService.updateTeam(parentMatch.getId(), match.getTeamB(), "teama");
                else if (parentMatch.getTeamBObject() == null)
                    MatchService.updateTeam(parentMatch.getId(), match.getTeamB(), "teamb");
            }
        }
        matchRepository.reload();
        return "index";
    }

    @GetMapping("/toggleMatchActive")
    public String getToggleMatchActive (@RequestParam String id) {
        MatchRepository matchRepository = Repositories.getMatchRepository();
        long internalId = Long.parseLong(id);
        Match match = matchRepository.getMatch(internalId);
        MatchService.toggleActive(match);
        matchRepository.reload();
        return "index";
    }

    @GetMapping("/matches")
    public ModelAndView getMatches () {
        TeamRepository teamRepository = Repositories.getTeamRepository();
        List<Team> teams = teamRepository.getAllTeams();
        ModelAndView mav = new ModelAndView("matches");
        mav.addObject("teamA", teams.get (0));
        mav.addObject("teamAplayers", teams.get(0).getPlayers());
        mav.addObject("teamB", teams.get (1));
        mav.addObject("teamBplayers", teams.get(1).getPlayers());
        mav.addObject("teamC", teams.get (2));
        mav.addObject("teamCplayers", teams.get(2).getPlayers());
        mav.addObject("teamD", teams.get (3));
        mav.addObject("teamDplayers", teams.get(3).getPlayers());
        return mav;
    }

    @GetMapping("/allMatches")
    public ModelAndView getAllMatches () {
        MatchRepository matchController = Repositories.getMatchRepository();
        List<Match> matches = matchController.getAllMatches();
        ModelAndView mav = new ModelAndView("allmatches");
        mav.addObject("matches", matches);
        return mav;
    }

    @GetMapping("/currentMatch")
    public ModelAndView getCurrentMatch () {
        MatchRepository matchController = Repositories.getMatchRepository();
        Match currentMatch = matchController.getCurrentMatch();
        ModelAndView mav = new ModelAndView("currentMatch");
        Team teamA = currentMatch.getTeamAObject();
        Team teamB = currentMatch.getTeamBObject();
        mav.addObject("teamA", teamA);
        mav.addObject("teamAplayers", teamA.getPlayers());
        mav.addObject("teamB", teamB);
        mav.addObject("teamBplayers", teamB.getPlayers());
        return mav;
    }

    @GetMapping("/player")
    public ModelAndView getPlayer (@RequestParam String id) {
        PlayerRepository playerRepository = Repositories.getPlayerRepository();
        TeamRepository teamRepository = Repositories.getTeamRepository();
        long internalId = Long.parseLong(id);
        Player player = playerRepository.get(internalId);
        Team team = teamRepository.getTeam(player.getTeamId());
        List<ChampionMastery> maestries = new ArrayList<>();
        int max = 25;
        for (int i=0; i<player.getMasteries().size(); i++) {
            if (i == max)
                break;
            maestries.add(player.getMasteries().get(i));
        }
        List<String> screenLabels = player.getScreens();
        List<Long> lps = player.getLpHistory();
        List<Integer> wins = player.getWinHistory();
        List<Integer> loses = player.getLosesHistory();
        List<Integer> matches = player.getMatchesHistory();
        Map<MaestryTier, List<ChampionMastery>> masteries = player.getMaestryTiers();
        ModelAndView mav = new ModelAndView("player");
        mav.addObject("player", player);
        mav.addObject("summonerLevel", "Lvl: " + player.getSummonerLevel());
        mav.addObject("teamIcon", team.getIcon());
        mav.addObject("maestries", maestries);
        mav.addObject("screens", screenLabels);
        mav.addObject("wins" , wins);
        mav.addObject("loses", loses);
        mav.addObject("matches", matches);
        mav.addObject("lps", lps);
        mav.addObject("S", masteries.get(MaestryTier.S));
        mav.addObject("A", masteries.get(MaestryTier.A));
        mav.addObject("B", masteries.get(MaestryTier.B));
        mav.addObject("C", masteries.get(MaestryTier.C));
        mav.addObject("D", masteries.get(MaestryTier.D));
        mav.addObject("F", masteries.get(MaestryTier.F));
        return mav;
    }

    @GetMapping("/team")
    public ModelAndView getTeam (@RequestParam String id) {
        TeamRepository teamRepository = Repositories.getTeamRepository();
        long internalId = Long.parseLong(id);
        Team team = teamRepository.getTeam(internalId);
        List<String> screenLabels = team.getScreens();
        List<Long> lps = team.getLpHistory();
        List<Integer> wins = team.getWinHistory();
        List<Integer> loses = team.getLosesHistory();
        List<Integer> matches = team.getMatchesHistory();
        ModelAndView mav = new ModelAndView("team");
        mav.addObject("team", team);
        mav.addObject("screens", screenLabels);
        mav.addObject("wins" , wins);
        mav.addObject("loses", loses);
        mav.addObject("matches", matches);
        mav.addObject("lps", lps);
        return mav;
    }

    @GetMapping("/table")
    public ModelAndView getMatchTable () {
        ModelAndView mav = new ModelAndView("table");
        mav.addObject("matchStats", null);
        return mav;
    }

    @GetMapping("/winner")
    public ModelAndView getWinner () {
        ModelAndView mav = new ModelAndView("winner");
        mav.addObject("winnerIcon", null);
        mav.addObject("winnerName", null);
        return mav;
    }

    private boolean teamExists (List<MatchStats> matchStats, Team team) {
        for (MatchStats matchStat : matchStats)
            if (matchStat.getTeamId() == team.getId())
                return true;
        return false;
    }
}
