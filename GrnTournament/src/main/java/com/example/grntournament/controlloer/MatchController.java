package com.example.grntournament.controlloer;

import grn.database.pojo.*;
import grn.database.repository.Repositories;
import grn.database.repository.TeamRepository;
import grn.database.service.MatchService;
import grn.database.repository.MatchRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class MatchController {

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
    public String getEditMatchSubmit (@ModelAttribute Match match, Model model) {
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

    @GetMapping("/currentMatch")
    public ModelAndView getCurrentMatch () {
        MatchRepository matchController = Repositories.getMatchRepository();
        Match currentMatch = matchController.getCurrentMatch();
        ModelAndView mav = new ModelAndView("currentMatch");
        Team teamA = currentMatch.getTeamAObject();
        Team teamB = currentMatch.getTeamBObject();
        mav.addObject("match" , currentMatch);
        mav.addObject("teamA", teamA);
        mav.addObject("teamAplayers", teamA.getPlayers());
        mav.addObject("teamB", teamB);
        mav.addObject("teamBplayers", teamB.getPlayers());
        return mav;
    }

    @GetMapping("/winner")
    public ModelAndView getWinner () {
        ModelAndView mav = new ModelAndView("winner");
        mav.addObject("winnerIcon", null);
        mav.addObject("winnerName", null);
        return mav;
    }

}
