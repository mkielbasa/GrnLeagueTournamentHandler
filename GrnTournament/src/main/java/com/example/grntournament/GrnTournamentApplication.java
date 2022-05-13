package com.example.grntournament;

import grn.database.ConnectionEstablisher;
import grn.database.repository.Repositories;
import grn.file.TextFileWriter;
import grn.http.HttpRequester;
import grn.twitch.TwitchBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class GrnTournamentApplication {

    public static void main(String[] args) {
        JVMShutdownHook jvmShutdownHook = new JVMShutdownHook();
        Runtime.getRuntime().addShutdownHook(jvmShutdownHook);
        SpringApplication.run(GrnTournamentApplication.class, args);
        ConnectionEstablisher.connect();
        HttpRequester.init();
        Repositories.init();
        TwitchBot.init();
    }

    private static class JVMShutdownHook extends Thread {
        public void run() {
            TextFileWriter.write(HttpRequester.getEndpointCalls()+"", new File("./GrnTournament/calls.log"));
        }
    }

}
