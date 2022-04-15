package grn.riot.lol;


import grn.error.ConsoleHandler;
import grn.json.JsonFileReader;
import grn.json.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class ChampionRepository implements JsonParser {

    private Map<Long, Champion> champions = new HashMap<>();

    public ChampionRepository (File jsonFile) {
        JSONObject jObject = JsonFileReader.read(jsonFile);
        parse(jObject);
    }

    public Champion getChampion (long id) {
        return champions.get(id);
    }

    @Override
    public void parse(JSONObject jObject) {
        JSONObject jData =(JSONObject) jObject.get("data");
        for (Object key : jData.keySet()) {
            String keyString = key.toString();
            JSONObject jChampion = (JSONObject) jData.get(keyString);
            Champion champion = new Champion();
            champion.parse(jChampion);
            champions.put(champion.getId(), champion);
            ConsoleHandler.handleInfo("Loaded champion: " + keyString);
        }
        ConsoleHandler.handleInfo("Loaded all champions (" + champions.keySet().size() + ")");
    }
}
