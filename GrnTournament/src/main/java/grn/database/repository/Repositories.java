package grn.database.repository;

import grn.datadragon.DataDragonRepository;
import grn.error.ConsoleHandler;
import grn.exception.EndpointException;

public class Repositories {

    private static DataDragonRepository dataDragonRepository;
    private static TeamRepository teamRepository;
    private static PlayerRepository playerRepository;
    private static MatchRepository matchRepository;
    private static ViewerScoreRepository viewerScoreRepository;

    public static void init () {
        try {
            dataDragonRepository = new DataDragonRepository();
            dataDragonRepository.init();
            teamRepository = new TeamRepository();
            teamRepository.init();
            playerRepository = new PlayerRepository(teamRepository);
            playerRepository.init();
            matchRepository = new MatchRepository();
            matchRepository.init();
            viewerScoreRepository = new ViewerScoreRepository();
            viewerScoreRepository.init();
        } catch (EndpointException e) {
            ConsoleHandler.handleException(e);
        }
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

    public static MatchRepository getMatchRepository() {
        return matchRepository;
    }

    public static ViewerScoreRepository getViewerScoreRepository() {
        return viewerScoreRepository;
    }
}
