package com.example.grntournament;

import grn.database.ConnectionEstablisher;
import grn.database.repository.PlayerRepository;
import grn.database.repository.TeamRepository;
import grn.database.repository.ViewerScoreRepository;
import grn.riot.lol.MatchController;
import grn.datadragon.DataDragonRepository;
import grn.twitch.ChatReader;
import grn.twitch.TwitchBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GrnTournamentApplication {

    private static DataDragonRepository metadataRepository;
    private static TeamRepository teamRepository;
    private static PlayerRepository playerRepository;
    private static MatchController matchController;

    public static void main(String[] args) {
        SpringApplication.run(GrnTournamentApplication.class, args);
        ConnectionEstablisher.connect();
        metadataRepository = new DataDragonRepository();
        teamRepository = new TeamRepository();
        playerRepository = new PlayerRepository(teamRepository);
        matchController = new MatchController();
        matchController.init();
        TwitchBot.init();
        ViewerScoreRepository.init();
        ViewerScoreRepository.reload();
        ChatReader.parse();
    }

    public static DataDragonRepository getMetadataRepository() {
        return metadataRepository;
    }

    public static TeamRepository getTeamRepository() {
        return teamRepository;
    }

    public static PlayerRepository getPlayerRepository() {
        return playerRepository;
    }

    public static MatchController getMatchController() {
        return matchController;
    }
}
