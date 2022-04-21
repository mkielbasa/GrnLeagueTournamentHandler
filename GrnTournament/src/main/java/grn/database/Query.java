package grn.database;

import grn.error.ConsoleHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Query {

    private String sql;
    private List<Object> params = new ArrayList<>();

    public Query (String sql) {
        this.sql = sql;
    }

    public void setParams (Object... params) {
        for (Object object : params)
            this.params.add(object);
    }

    public List<QueryRow> execute () {
        List<QueryRow> rows = new ArrayList<>();
        Connection conn = ConnectionEstablisher.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setParams(ps);
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                while (rs.next()) {
                    QueryRow row = new QueryRow();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++)
                        row.addValue(rs.getObject(i));
                    rows.add(row);
                }
            }
        } catch (SQLException e) {
            ConsoleHandler.handleException(e);
        }
        return rows;
    }

    private void setParams(PreparedStatement ps) {
        try {
            for (int i = 0; i < params.size(); i++)
                ps.setObject(i + 1, params.get(i));
        } catch (SQLException e) {
            ConsoleHandler.handleException(e);
        }
    }
}
