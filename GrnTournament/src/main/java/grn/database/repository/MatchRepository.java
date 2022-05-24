package grn.database.repository;

import grn.database.pojo.Match;
import grn.database.service.MatchService;
import grn.matchengine.CupEngine;
import grn.matchengine.LeagueEngine;
import grn.matchengine.MatchEngine;
import grn.matchengine.MatchType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchRepository implements Repository {

    public Match currentMatch;

    private Map<Integer, Map<Long, Match>> matches = new HashMap<>();

    public void init () {
        reload();
    }

    @Override
    public void reload() {
        matches.clear();
        List<Match> allMatches = MatchService.getAllMatches();
        for (Match match : allMatches) {
            int tier = match.getGroup ();
            if (!matches.containsKey(tier))
                matches.put(tier, new HashMap<>());
            matches.get(tier).put(match.getId(), match);
        }
        currentMatch = getActiveMatch();
    }

    private Match getActiveMatch () {
        for (int tier : matches.keySet())
            for (Match match : matches.get(tier).values())
                if (match.isActive())
                    return match;
        return null;
    }

    public Match getFinalMatch (int tier) {
        Map<Long, Match> tierMatches = matches.get(tier);
        for (Match match : tierMatches.values())
            if (match.getParent() == 0)
                return match;
        return null;
    }

    public List<Match> getMatchesWithParent (int tier, Match parentMatch) {
        List<Match> childrenMatches = new ArrayList<>();
        Map<Long, Match> tierMatches = matches.get(tier);
        for (Match match : tierMatches.values())
            if (match.getParent() == parentMatch.getId())
                childrenMatches.add(match);
        return  childrenMatches;
    }

    public List<Match> getAllMatches () {
        return new ArrayList<>();
    }

    public Match getMatch (long id) {
        for (int tier : matches.keySet())
            for (Match match : matches.get(tier).values())
                if (match.getId() == id)
                    return match;
        return null;
    }

    public Match getCurrentMatch() {
        if (currentMatch == null) {
            Match mock = new Match();
            mock.setResult("NO-MATCH");
            return new Match();
        }
        return currentMatch;
    }
}
