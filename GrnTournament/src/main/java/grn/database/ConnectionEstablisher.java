package grn.database;

import grn.error.ConsoleHandler;
import grn.properties.PropertiesHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionEstablisher {

    public static Connection connect () {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", PropertiesHandler.instance().getDbUser());
        connectionProps.put("password", PropertiesHandler.instance().getDbPass());
        try {
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/grnt", connectionProps);
        } catch (SQLException e) {
            ConsoleHandler.handleException(e);
        }
        return null;
    }
}
