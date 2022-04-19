package com.example.grntournament.controlloer;

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
}
