package grn.database;

import grn.error.ConsoleHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Delete {

    private String sql;
    private List<Object> params = new ArrayList<>();

    public Delete(String sql) {
        this.sql =  sql;
    }

    public void setParams(Object... values) {
        for (Object object : values)
            this.params.add(object);
    }

    public void execute () {
        try (Connection conn = ConnectionEstablisher.connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            setParams(ps);
            ps.execute();
        } catch (SQLException e) {
            ConsoleHandler.handleException(e);
        }
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
