package com.example.grntournament;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.credentialmanager.identityprovider.TwitchIdentityProvider;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import grn.database.ConnectionEstablisher;
import grn.database.pojo.Match;
import grn.database.pojo.Team;
import grn.database.pojo.ViewerScore;
import grn.database.repository.PlayerRepository;
import grn.database.repository.TeamRepository;
import grn.database.repository.ViewerScoreRepository;
import grn.database.service.MatchService;
import grn.database.service.PlayerService;
import grn.datadragon.DataDragonHandler;
import grn.error.ConsoleHandler;
import grn.file.FileDownloader;
import grn.http.HttpRequester;
import grn.properties.PropertiesHandler;
import grn.riot.lol.MatchController;
import grn.riot.lol.MetadataRepository;
import grn.riot.lol.endpoint.SummonerEndpoint;
import grn.sound.SoundPlayer;
import grn.twitch.TwitchBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@SpringBootApplication
public class GrnTournamentApplication {

    private static MetadataRepository metadataRepository;
    private static TeamRepository teamRepository;
    private static PlayerRepository playerRepository;
    private static MatchController matchController;

    public static void main(String[] args) {
        SpringApplication.run(GrnTournamentApplication.class, args);
        ConnectionEstablisher.connect();
        metadataRepository = new MetadataRepository();
        teamRepository = new TeamRepository();
        playerRepository = new PlayerRepository(teamRepository);
        matchController = new MatchController();
        matchController.init();
        TwitchBot.init();
        ViewerScoreRepository.init();
        ViewerScoreRepository.reload();
    }

    public static MetadataRepository getMetadataRepository() {
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
