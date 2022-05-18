package grn.matchengine;

import grn.database.pojo.Match;
import grn.database.service.MatchService;
import grn.error.ConsoleHandler;

import java.util.*;

public class CupEngine extends  MatchEngine {

    private List<Integer> levels;

    public CupEngine(MatchType matchType, int teamQuantity) {
        super(matchType, getMatchQuantity(teamQuantity));
        this.levels = getLevels(teamQuantity);
    }

    private static int getMatchQuantity (int teamQuantity) {
        int quantity = 0;
        int factor = teamQuantity;
        while (factor != 1) {
            factor = factor / 2;
            quantity += factor;
        }
        return  quantity;
    }

    private static List<Integer> getLevels (int teamQuantity) {
        List<Integer> levels = new ArrayList<>();
        int factor = teamQuantity;
        while (factor != 1) {
            factor = factor / 2;
            levels.add(factor);
        }
        Collections.reverse(levels);
        return levels;
    }

    @Override
    public void initializeMatches() {
        createMatches(0, 0);
    }

    private void createMatches (int level, long parentId) {
        int maxLevel = levels.size()-1;
        if (level > maxLevel)
            return;
        if (level == 0) {
            long matchId = createMatch(0);
            createMatches(1, matchId);
        } else {
            for (int i=0; i<2; i++) {
                long matchId = createMatch(parentId);
                createMatches(level+1, matchId);
            }
        }
    }

    private long createMatch (long parentId) {
        return MatchService.registerMatch(parentId);
    }

}
