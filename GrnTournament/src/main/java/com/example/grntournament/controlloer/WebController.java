package com.example.grntournament.controlloer;

import com.example.grntournament.GrnTournamentApplication;
import grn.database.repository.PlayerRepository;
import grn.database.repository.Repositories;
import grn.database.repository.ViewerScoreRepository;
import grn.database.service.ViewerScoreService;
import grn.error.ConsoleHandler;
import grn.exception.EndpointException;
import grn.exception.OutdatedApiKeyException;
import grn.riot.lol.MatchController;
import grn.sound.SoundPlayer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @GetMapping("/reloadMatches")
    public String reloadMatches () throws EndpointException {
        MatchController matchController = Repositories.getMatchRepository();
        matchController.finishCurrentMatch();
        ConsoleHandler.handleInfo("MatchesReloaded");
        SoundPlayer.playSound("./GrnTournament/ding.wav");
        return "Reloaded";
    }

    @GetMapping("/reloadPlayers")
    public String reloadPlayers () throws EndpointException {
        PlayerRepository playerRepository = Repositories.getPlayerRepository();
        playerRepository.initMaestries();
        playerRepository.initLeagues();
        ConsoleHandler.handleInfo("PlayersReloaded");
        SoundPlayer.playSound("./GrnTournament/ding.wav");
        return "Reloaded";
    }

    @GetMapping("/clearViewerScores")
    public String clearViewerScores () {
        ViewerScoreService.clear();
        ConsoleHandler.handleInfo("ScoresCleared");
        SoundPlayer.playSound("./GrnTournament/ding.wav");
        return "Reloaded";
    }

    @GetMapping("stop")
    public String stop () {
        ViewerScoreRepository viewerScoreRepository = Repositories.getViewerScoreRepository();
        viewerScoreRepository.saveScores();
        System.exit(0);
        return "STOPPED";
    }

}
