package grn.endpoint;

import grn.http.HttpRequester;
import grn.properties.PropertiesHandler;
import org.json.simple.JSONArray;

public class LeagueEndpoint extends EndpointRequest {

    private final String summonerId;

    public LeagueEndpoint (String summonerId) {
        super();
        this.summonerId = summonerId;
    }

    @Override
    protected String[] buildParams() {
        return new String[] {summonerId, apiKey};
    }

    @Override
    protected String buildURL() {
        return "https://eun1.api.riotgames.com/lol/league/v4/entries/by-summoner/%s?api_key=%s";
    }

    @Override
    protected String buildEndpointKey() {
        return "LEAGUE";
    }
}
