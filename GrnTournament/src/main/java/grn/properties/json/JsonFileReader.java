package grn.properties.json;

import grn.error.ConsoleHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class JsonFileReader {

    public static JSONObject read (File file) {
        ConsoleHandler.handleInfo("Parsing JSON file " + file.getAbsolutePath() + "...");
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(file)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            return jsonObject;
        } catch (IOException e) {
            ConsoleHandler.handleException(e);
        } catch (ParseException e) {
            ConsoleHandler.handleException(e);
        }
        return null;
    }
}
