package grn.properties;

import grn.error.ConsoleHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesHandler {

    private static PropertiesHandler instance;

    private Properties props;

    private PropertiesHandler () {
        props = new Properties();
        try {
            props.load(new FileInputStream("./GrnTournament/system.properties"));
        } catch (IOException e) {
            ConsoleHandler.handleException(e);
        }
    }

    public static PropertiesHandler instance () {
        if (instance == null)
            instance = new PropertiesHandler();
        return instance;
    }

    public String getDataDragonUrl () {
        return props.getProperty("dataDragonURL");
    }

    public String getDataFileDirectory () {
        return props.getProperty("dataDragonFileDirectory");
    }
}
