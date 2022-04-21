package grn.database;

import grn.error.ConsoleHandler;
import grn.properties.PropertiesHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionEstablisher {

    private static Connection conn = null;

    public static void connect () {
        Properties connectionProps = new Properties();
        connectionProps.put("user", PropertiesHandler.instance().getDbUser());
        connectionProps.put("password", PropertiesHandler.instance().getDbPass());
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/grnt", connectionProps);
        } catch (SQLException e) {
            ConsoleHandler.handleException(e);
        }
    }

    public static Connection getConnection () {
        return conn;
    }

    public static void disconnect () {
        try {
            conn.close();
        } catch (SQLException e) {
            ConsoleHandler.handleException(e);
        }
    }
}
