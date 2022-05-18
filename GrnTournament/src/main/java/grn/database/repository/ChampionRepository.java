package grn.database.repository;


import grn.error.ConsoleHandler;
import grn.properties.json.JsonFileReader;
import grn.properties.json.JsonParser;
import grn.database.pojo.Champion;
import org.json.simple.JSONObject;

import java.io.File;
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
        }
        ConsoleHandler.handleInfo("Loaded all champions (" + champions.keySet().size() + ")");
    }
}
