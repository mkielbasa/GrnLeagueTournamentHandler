package com.example.grntournament;

import grn.database.ConnectionEstablisher;
import grn.database.repository.PlayerRepository;
import grn.database.repository.TeamRepository;
import grn.database.service.PlayerService;
import grn.datadragon.DataDragonHandler;
import grn.file.FileDownloader;
import grn.http.HttpRequester;
import grn.properties.PropertiesHandler;
import grn.riot.lol.MetadataRepository;
import grn.riot.lol.endpoint.SummonerEndpoint;
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

    public static void main(String[] args) {
        SpringApplication.run(GrnTournamentApplication.class, args);
        metadataRepository = new MetadataRepository();
        teamRepository = new TeamRepository();
        playerRepository = new PlayerRepository(teamRepository);

    }

    public static MetadataRepository getMetadataRepository() {
        return metadataRepository;
    }
}
