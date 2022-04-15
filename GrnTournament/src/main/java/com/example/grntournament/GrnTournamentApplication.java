package com.example.grntournament;

import grn.datadragon.DataDragonHandler;
import grn.file.FileDownloader;
import grn.properties.PropertiesHandler;
import grn.riot.lol.MetadataRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GrnTournamentApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrnTournamentApplication.class, args);
        MetadataRepository metadataRepository = new MetadataRepository();

    }

}
