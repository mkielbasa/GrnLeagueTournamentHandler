package grn.endpoint;

import grn.http.HttpRequester;
import grn.properties.PropertiesHandler;
import org.json.simple.JSONArray;

public class ChampionMasteryEndpoint extends EndpointRequest {

    private String summonerId;
    private String server;

    public ChampionMasteryEndpoint(String summonerId, String server) {
        super();
        this.summonerId = summonerId;
        this.server = server;
        this.params = buildParams();
    }

    @Override
    protected String[] buildParams() {
        return new String[] {server,summonerId, apiKey};
    }

    @Override
    protected String buildURL() {
        return "https://%s.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-summoner/%s?api_key=%s";
    }

    @Override
    protected String buildEndpointKey() {
        return "RIOT";
    }
}
