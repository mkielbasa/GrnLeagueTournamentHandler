package com.example.grntournament;

import grn.database.ConnectionEstablisher;
import grn.database.repository.Repositories;
import grn.twitch.TwitchBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GrnTournamentApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrnTournamentApplication.class, args);
        ConnectionEstablisher.connect();
        Repositories.init();
        TwitchBot.init();
    }

}
