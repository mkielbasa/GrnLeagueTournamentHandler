package com.example.grntournament.controlloer;

import grn.database.pojo.*;
import grn.database.repository.MatchRepository;
import grn.database.repository.PlayerRepository;
import grn.database.repository.Repositories;
import grn.database.repository.TeamRepository;
import grn.database.service.MatchService;
import grn.database.service.PlayerService;
import grn.exception.EndpointException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class PlayerController {

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

    @GetMapping("/player")
    public ModelAndView getPlayer (@RequestParam String id) {
        ModelAndView mav = new ModelAndView("player");
        long internalId = Long.parseLong(id);
        buildPlayerModel(mav, internalId);
        return mav;
    }

    @GetMapping("/currentPlayer")
    public ModelAndView getPlayer () {
        PlayerRepository playerRepository = Repositories.getPlayerRepository();
        Player currentPlayer = playerRepository.getCurrentPlayer();
        ModelAndView mav = new ModelAndView("player");
        buildPlayerModel(mav, currentPlayer.getInternalId());
        return mav;
    }

    @GetMapping("/toggleCurrentPlayer")
    public String getToggleMatchActive (@RequestParam String id) {
        PlayerRepository playerRepository = Repositories.getPlayerRepository();
        long internalId = Long.parseLong(id);
        playerRepository.setCurrentPlayer (internalId);
        Player currentPlayer = playerRepository.getCurrentPlayer();
        ModelAndView mav = new ModelAndView("player");
        buildPlayerModel(mav, currentPlayer.getInternalId());
        return "index";
    }

    private void buildPlayerModel (ModelAndView mav, long playerId) {
        PlayerRepository playerRepository = Repositories.getPlayerRepository();
        TeamRepository teamRepository = Repositories.getTeamRepository();
        Player player = playerRepository.get(playerId);
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
    }

}
