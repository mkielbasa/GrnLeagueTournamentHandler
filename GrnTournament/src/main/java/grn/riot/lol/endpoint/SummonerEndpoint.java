package grn.riot.lol.endpoint;

import grn.error.ConsoleHandler;
import grn.http.HttpRequester;
import grn.properties.PropertiesHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SummonerEndpoint {

    public static JSONObject getSummonerByName (String summonerName) {
        try {
            String apiKey = PropertiesHandler.instance().getRiotApiKey();
            String summoner = URLEncoder.encode(summonerName, "UTF-8");
            String url = "https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/%s?api_key=%s";
            return (JSONObject) HttpRequester.doRequest(url, summoner, apiKey);
        } catch (UnsupportedEncodingException e) {
            ConsoleHandler.handleException(e);
        }
        return null;
    }

}
