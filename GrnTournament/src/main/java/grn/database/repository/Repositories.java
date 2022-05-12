package grn.database.repository;

import grn.datadragon.DataDragonRepository;
import grn.riot.lol.MatchController;

public class Repositories {

    private static DataDragonRepository dataDragonRepository;
    private static TeamRepository teamRepository;
    private static PlayerRepository playerRepository;
    private static MatchController matchRepository;
    private static ViewerScoreRepository viewerScoreRepository;

    public static void init () {
        dataDragonRepository.init();
        teamRepository.init();
        playerRepository.init();
        matchRepository.init();
        viewerScoreRepository.init();
    }

    public static PlayerRepository getPlayerRepository () {
        return playerRepository;
    }

    public static TeamRepository getTeamRepository() {
        return teamRepository;
    }

    public static DataDragonRepository getDataDragonRepository() {
        return dataDragonRepository;
    }

    public static MatchController getMatchRepository() {
        return matchRepository;
    }

    public static ViewerScoreRepository getViewerScoreRepository() {
        return viewerScoreRepository;
    }
}
