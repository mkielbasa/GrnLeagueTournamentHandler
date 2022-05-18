package grn.database.repository;

import grn.database.pojo.Match;
import grn.matchengine.LeagueEngine;
import grn.matchengine.MatchEngine;
import grn.matchengine.MatchType;

import java.util.ArrayList;
import java.util.List;

public class MatchRepository implements Repository {

    public Match currentMatch;

    public void init () {
        MatchEngine engine = new LeagueEngine(8);
        engine.clearMatches();
        engine.initializeMatches();
    }

    @Override
    public void reload() {

    }

    public List<Match> getAllMatches () {
        return new ArrayList<>();
    }

    public Match getCurrentMatch() {
        return currentMatch;
    }
}
