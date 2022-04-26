package grn.database.service;

import grn.database.*;
import grn.database.pojo.Team;
import grn.database.pojo.ViewerScore;

import java.util.ArrayList;
import java.util.List;

public class ViewerScoreService {

    public static List<ViewerScore> getAllScores () {
        List<ViewerScore> scores = new ArrayList<>();
        String sql = "select * from tournament.viewerscore";
        Query query = new Query(sql);
        List<QueryRow> rows = query.execute();
        for (QueryRow row : rows) {
            ViewerScore score = new ViewerScore();
            score.fromQueryRow(row);
            scores.add(score);
        }
        return scores;
    }

    public static void clear () {
        String sql = "delete from tournament.viewerscore";
        Delete delete = new Delete(sql);
        delete.execute();
    }

    public static void register (ViewerScore score) {
        Insert insert = new Insert("tournament.viewerscore");
        insert.setColumns("viewer", "teamid", "score");
        insert.setValues(score.getViewer(), score.getTeamId(), score.getScore());
        insert.execute();
    }

    public static void update (ViewerScore score) {
        String sql = "update tournament.viewerscore set score = ? where viewer = ? and teamid = ?";
        Update update = new Update(sql);
        update.setParams(score.getScore(), score.getViewer(), score.getTeamId());
        update.execute();
    }
}
