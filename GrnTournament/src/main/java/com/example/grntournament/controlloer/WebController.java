package com.example.grntournament.controlloer;

import com.example.grntournament.GrnTournamentApplication;
import grn.database.repository.PlayerRepository;
import grn.error.ConsoleHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @GetMapping("/reloadMatches")
    public String reloadMatches () {
        ConsoleHandler.handleInfo("MatchesReloaded");
        return "Reloaded";
    }

    @GetMapping("/reloadPlayers")
    public String reloadPlayers () {
        PlayerRepository playerRepository = GrnTournamentApplication.getPlayerRepository();
        playerRepository.updatePlayerMaestries();
        playerRepository.updatePlayerLeagues();
        ConsoleHandler.handleInfo("PlayersReloaded");
        return "Reloaded";
    }
}