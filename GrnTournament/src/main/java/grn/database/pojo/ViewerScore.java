package grn.database.pojo;

import grn.database.QueryRow;

public class ViewerScore {

    private String viewer;
    private long teamId;
    private long score;

    public void fromQueryRow (QueryRow row) {
        this.viewer = (String) row.get(1);
        this.teamId = (long) row.get(2);
        this.score = (long) row.get(3);
    }

    public String getViewer() {
        return viewer;
    }

    public void setViewer(String viewer) {
        this.viewer = viewer;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
