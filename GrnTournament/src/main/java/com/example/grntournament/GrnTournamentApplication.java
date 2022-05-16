package com.example.grntournament;

import grn.database.ConnectionEstablisher;
import grn.database.repository.Repositories;
import grn.error.ConsoleHandler;
import grn.file.TextFileWriter;
import grn.http.HttpRequester;
import grn.twitch.TwitchBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.io.File;
import java.net.URI;

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
        launchIndex();
    }

    private static void launchIndex () {
        System.setProperty("java.awt.headless", "false");
        Desktop desktop = Desktop.getDesktop();
        try{
            desktop.browse(new URI("http://localhost:8080/index"));
        } catch(Exception e){
            ConsoleHandler.handleException(e);
        }
    }

    private static class JVMShutdownHook extends Thread {
        public void run() {
            TextFileWriter.write(HttpRequester.getEndpointCalls()+"", new File("./GrnTournament/calls.log"));
        }
    }

}
