package com.example.grntournament.controlloer;

import grn.database.pojo.Team;
import grn.database.repository.PlayerRepository;
import grn.database.repository.Repositories;
import grn.database.repository.TeamRepository;
import grn.database.service.TeamService;
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

@Controller
public class TeamController {

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
}
