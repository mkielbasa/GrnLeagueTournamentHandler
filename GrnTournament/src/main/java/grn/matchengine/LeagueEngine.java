package grn.matchengine;

import grn.database.Math;
import grn.database.pojo.Match;
import grn.database.service.MatchService;
import grn.error.ConsoleHandler;

import java.util.List;

public class LeagueEngine extends  MatchEngine {

    public LeagueEngine(int teamQuantity) {
        super(MatchType.BO1, getMatchQuantity (teamQuantity));
    }

    private static int getMatchQuantity (int teamsQuantity) {
        int n = teamsQuantity;
        int k = 2;
        return Math.factorial(n) / Math.factorial(n-k);
    }

    @Override
    public void initializeMatches() {
        for (int i=0; i<matchQuantity; i++) {
            long matchId = MatchService.registerMatch(0);
            ConsoleHandler.handleInfo("Registered match " + matchId);
        }
    }

}
