package grn.matchengine;

import grn.database.pojo.Match;
import grn.database.pojo.Team;
import grn.database.service.MatchService;

import java.util.ArrayList;
import java.util.List;

public abstract class MatchEngine {

    protected MatchType matchType;
    protected int matchQuantity;

    public MatchEngine(MatchType matchType, int matchQuantity) {
        this.matchType = matchType;
        this.matchQuantity = matchQuantity;
    }

    public void clearMatches () {
        MatchService.clearMatches();
    }

    public abstract void initializeMatches ();

    public List<Match> getAllMatches () {
        return new ArrayList<>();
    }

    public void setCurrentMatch (Match match) {

    }

    public void setTeamA (Team team, Match match) {
        MatchService.updateTeam(match.getId(), team, "teama");
    }

    public void setTeamB (Team team, Match match) {
        MatchService.updateTeam(match.getId(), team, "teamb");
    }

    public void updateScore (Match match, int teamAScore, int teamBScore) {
        MatchService.updateScore(match.getId(), teamAScore, teamBScore);
    }

    public void finishMatch (Match match) {
        Team teamA = match.getTeamAObject();
        Team teamB = match.getTeamBObject();
        Team winner = null;
        if (match.getTeamAScore() == match.getTeamBScore())
            return;
        if (match.getTeamAScore() > match.getTeamBScore())
            winner = teamA;
        if (match.getTeamBScore() > match.getTeamAScore())
            winner = teamB;

        MatchService.finishMatch(match.getId(), winner);
    }


}
