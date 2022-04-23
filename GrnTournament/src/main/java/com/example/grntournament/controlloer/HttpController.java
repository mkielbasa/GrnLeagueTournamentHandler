package com.example.grntournament.controlloer;

import com.example.grntournament.GrnTournamentApplication;
import grn.database.pojo.ChampionMastery;
import grn.database.pojo.Match;
import grn.database.pojo.Player;
import grn.database.pojo.Team;
import grn.database.repository.PlayerRepository;
import grn.database.repository.TeamRepository;
import grn.error.ConsoleHandler;
import grn.riot.lol.MatchController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HttpController {

    @GetMapping("/matches")
    public ModelAndView getMatches () {
        TeamRepository teamRepository = GrnTournamentApplication.getTeamRepository();
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
        mav.addObject("teamE", teams.get (4));
        mav.addObject("teamEplayers", teams.get(4).getPlayers());
        return mav;
    }

    @GetMapping("/allMatches")
    public ModelAndView getAllMatches () {
        MatchController matchController = GrnTournamentApplication.getMatchController();
        List<Match> matches = matchController.getAllMatches();
        ModelAndView mav = new ModelAndView("allmatches");
        mav.addObject("matches", matches);
        return mav;
    }

    @GetMapping("/currentMatch")
    public ModelAndView getCurrentMatch () {
        MatchController matchController = GrnTournamentApplication.getMatchController();
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
        PlayerRepository playerRepository = GrnTournamentApplication.getPlayerRepository();
        TeamRepository teamRepository = GrnTournamentApplication.getTeamRepository();
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
}
