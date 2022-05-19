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
        ModelAndView mav = new ModelAndView("teams");
        mav.addObject("teams", teams);
        return mav;
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
        MatchRepository matchRepository = Repositories.getMatchRepository();
        Match finalMatch = matchRepository.getFinalMatch(1);
        ModelAndView mav = new ModelAndView("tree4");
        mav.addObject("final", finalMatch);
        List<Match> starter1 = matchRepository.getMatchesWithParent(1, finalMatch);
        mav.addObject("starter1", starter1.get(0));
        mav.addObject("starter2", starter1.get(1));
        return mav;
    }

    @GetMapping("/tree8")
    public ModelAndView getTree8 () {
        ModelAndView mav = new ModelAndView("tree8");
        mav.addObject("final", null);
        mav.addObject("semi-final-1", null);
        mav.addObject("semi-final-2", null);
        mav.addObject("starter-1", null);
        mav.addObject("starter-2", null);
        mav.addObject("starter-3", null);
        mav.addObject("starter-4", null);
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
        int max = 10;
        for (int i=0; i<player.getMasteries().size(); i++) {
            if (i == max)
                break;
            maestries.add(player.getMasteries().get(i));
        }
        ModelAndView mav = new ModelAndView("player");
        mav.addObject("player", player);
        mav.addObject("summonerLevel", "Lvl: " + player.getSummonerLevel());
        mav.addObject("teamIcon", team.getIcon());
        mav.addObject("maestries", maestries);
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
