package com.example.grntournament.controlloer;

import com.example.grntournament.GrnTournamentApplication;
import grn.database.pojo.*;
import grn.database.repository.PlayerRepository;
import grn.database.repository.Repositories;
import grn.database.repository.TeamRepository;
import grn.database.service.MatchService;
import grn.database.service.PlayerService;
import grn.database.service.TeamService;
import grn.error.ConsoleHandler;
import grn.exception.EndpointException;
import grn.riot.lol.MatchController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collection;
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
        MatchController matchController = Repositories.getMatchRepository();
        List<Match> matches = matchController.getAllMatches();
        ModelAndView mav = new ModelAndView("allmatches");
        mav.addObject("matches", matches);
        return mav;
    }

    @GetMapping("/currentMatch")
    public ModelAndView getCurrentMatch () {
        MatchController matchController = Repositories.getMatchRepository();
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
        List<MatchStats> matchStats = MatchService.getMatchStats();
        TeamRepository teamRepository = Repositories.getTeamRepository();
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

        ModelAndView mav = new ModelAndView("table");
        mav.addObject("matchStats", matchStats);
        return mav;
    }

    @GetMapping("/winner")
    public ModelAndView getWinner () {
        List<MatchStats> matchStats = MatchService.getMatchStats();
        TeamRepository teamRepository = Repositories.getTeamRepository();
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

        MatchStats winnerStats = matchStats.get(0);
        Team winner = teamRepository.getTeam(winnerStats.getTeamId());

        ModelAndView mav = new ModelAndView("winner");
        mav.addObject("winnerIcon", winner.getIcon());
        mav.addObject("winnerName", winner.getName());
        return mav;
    }

    private boolean teamExists (List<MatchStats> matchStats, Team team) {
        for (MatchStats matchStat : matchStats)
            if (matchStat.getTeamId() == team.getId())
                return true;
        return false;
    }
}
