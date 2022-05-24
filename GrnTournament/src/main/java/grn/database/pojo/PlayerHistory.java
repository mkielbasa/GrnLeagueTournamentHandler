package grn.database.pojo;

import grn.database.QueryRow;

import java.sql.Timestamp;

public class PlayerHistory {

    private long playerId;
    private Timestamp screenTime;
    private long lp;
    private int wins;
    private int loses;
    private int matches;

    public void fromQueryRow (QueryRow row) {
        this.playerId = (long) row.get(1);
        this.screenTime = new Timestamp((long)row.get(3));
        this.lp = (int) row.get(2);
        this.wins = (int) row.get(4);
        this.loses = (int) row.get(5);
        this.matches = (int) row.get(6);
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
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
