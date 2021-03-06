package grn.database;

import grn.error.ConsoleHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Insert {

    private String table;
    private List<String> columns = new ArrayList<>();
    private List<Object> params = new ArrayList<>();

    public Insert(String table) {
        this.table =  table;
    }

    public void setColumns (String... columns) {
        for (String column : columns)
            this.columns.add(column);
    }

    public void setValues (Object... values) {
        for (Object object : values)
            this.params.add(object);
    }

    private String buildSQL () {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into " + table);
        sb.append("(");
        for (int i = 0; i < columns.size(); i++) {
            sb.append(columns.get(i));
            if (i < columns.size() - 1)
                sb.append(",");
        }
        sb.append(")");
        sb.append(" values (");
        for (int i = 0; i < columns.size(); i++) {
            sb.append("?");
            if (i < columns.size() - 1)
                sb.append(",");
        }
        sb.append(")");
        return sb.toString();
    }

    public void execute () {
        Connection conn = ConnectionEstablisher.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(buildSQL())) {
            setParams(ps);
            ps.execute();
        } catch (SQLException e) {
            ConsoleHandler.handleException(e);
        }
    }

    public long executeReturning () {
        Connection conn = ConnectionEstablisher.getConnection();
        String sql = buildSQL() + " RETURNING ID";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setParams(ps);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getLong(1);
            else
                return 0;
        } catch (SQLException e) {
            ConsoleHandler.handleException(e);
        }
        return 0;
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
