package grn.endpoint;

import grn.http.HttpRequester;
import grn.properties.PropertiesHandler;
import org.json.simple.JSONArray;

public class ChampionMasteryEndpoint extends EndpointRequest {

    private String summonerId;

    public ChampionMasteryEndpoint(String summonerId) {
        super();
        this.summonerId = summonerId;
    }

    @Override
    protected String[] buildParams() {
        return new String[] {summonerId, apiKey};
    }

    @Override
    protected String buildURL() {
        return "https://eun1.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-summoner/%s?api_key=%s";
    }

    @Override
    protected String buildEndpointKey() {
        return "CHAMPION-MASTERY";
    }
}
