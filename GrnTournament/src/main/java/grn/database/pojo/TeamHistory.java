package grn.database.pojo;

import grn.database.QueryRow;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

public class TeamHistory {

    private Timestamp screenTime;
    private long teamId;
    private long lp;
    private int wins;
    private int loses;
    private int matches;

    public void fromQueryRow (QueryRow row) {
        this.teamId = (long) row.get(1);
        this.screenTime = new Timestamp((long)row.get(2));
        this.lp = (int) row.get(3);
        this.wins = (int) row.get(4);
        this.loses = (int) row.get(5);
        this.matches = (int) row.get(6);
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public Timestamp getScreenTime() {
        return screenTime;
    }

    public void setScreenTime(Timestamp screenTime) {
        this.screenTime = screenTime;
    }

    public long getLp() {
        return lp;
    }

    public void setLp(long lp) {
        this.lp = lp;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public int getMatches() {
        return matches;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }
}
