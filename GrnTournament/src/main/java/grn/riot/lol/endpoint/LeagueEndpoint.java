package grn.riot.lol.endpoint;

import grn.http.HttpRequester;
import grn.properties.PropertiesHandler;
import org.json.simple.JSONArray;

public class LeagueEndpoint {

    public static JSONArray getLeagueEntries (String summonerId) {
        String apiKey = PropertiesHandler.instance().getRiotApiKey();
        String url = "https://eun1.api.riotgames.com/lol/league/v4/entries/by-summoner/%s?api_key=%s";
        return (JSONArray) HttpRequester.doRequest(url, summonerId, apiKey);
    }

}
