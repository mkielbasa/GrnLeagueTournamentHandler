package grn.riot.lol.endpoint;

import grn.error.ConsoleHandler;
import grn.http.HttpRequester;
import grn.properties.PropertiesHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MatchEndpoint {

    public static JSONArray getMatchIds (String pUUID) {
        String apiKey = PropertiesHandler.instance().getRiotApiKey();
        String url = "https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/%s/ids?start=0&count=20&api_key=%s";
        return (JSONArray) HttpRequester.doRequest(url, pUUID, apiKey);
    }

    public static JSONObject getMatchDetails (String matchId) {
        String apiKey = PropertiesHandler.instance().getRiotApiKey();
        String url = "https://europe.api.riotgames.com/lol/match/v5/matches/%s?api_key=%s";
        return (JSONObject) HttpRequester.doRequest(url, matchId, apiKey);
    }

}
