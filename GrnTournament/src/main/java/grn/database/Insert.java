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
        ConsoleHandler.handleInfo(sb.toString());
        return sb.toString();
    }

    public void execute () {
        try (Connection conn = ConnectionEstablisher.connect(); PreparedStatement ps = conn.prepareStatement(buildSQL())) {
            setParams(ps);
            ConsoleHandler.handleInfo("Params: " + params);
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
