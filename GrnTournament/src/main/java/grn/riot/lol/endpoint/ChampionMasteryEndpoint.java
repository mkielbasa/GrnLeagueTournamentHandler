package grn.riot.lol.endpoint;

import grn.http.HttpRequester;
import grn.properties.PropertiesHandler;
import org.json.simple.JSONArray;

public class ChampionMasteryEndpoint {

    public static JSONArray getChampionMaestries (String summonerId) {
        String apiKey = PropertiesHandler.instance().getRiotApiKey();
        String url = "https://eun1.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-summoner/%s?api_key=%s";
        return (JSONArray) HttpRequester.doRequest(url, summonerId, apiKey);
    }

}
