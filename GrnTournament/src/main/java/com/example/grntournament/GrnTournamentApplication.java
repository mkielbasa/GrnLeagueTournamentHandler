package com.example.grntournament;

import grn.datadragon.DataDragonHandler;
import grn.file.FileDownloader;
import grn.properties.PropertiesHandler;
import grn.riot.lol.MetadataRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GrnTournamentApplication {

    private static MetadataRepository metadataRepository;

    public static void main(String[] args) {
        SpringApplication.run(GrnTournamentApplication.class, args);
        metadataRepository = new MetadataRepository();

    }

    public static MetadataRepository getMetadataRepository() {
        return metadataRepository;
    }
}
