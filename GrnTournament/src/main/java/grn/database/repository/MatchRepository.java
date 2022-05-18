package grn.database.repository;

import grn.database.pojo.Match;

import java.util.ArrayList;
import java.util.List;

public class MatchRepository implements Repository {

    public Match currentMatch;

    public void init () {

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
