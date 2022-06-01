package grn.endpoint;

import grn.http.HttpRequester;
import grn.properties.PropertiesHandler;
import org.json.simple.JSONArray;

public class LeagueEndpoint extends EndpointRequest {

    private final String summonerId;
    private final String server;

    public LeagueEndpoint (String summonerId, String server) {
        super();
        this.summonerId = summonerId;
        this.server = server;
        this.params = buildParams();
    }

    @Override
    protected String[] buildParams() {
        return new String[] {server, summonerId, apiKey};
    }

    @Override
    protected String buildURL() {
        return "https://%s.api.riotgames.com/lol/league/v4/entries/by-summoner/%s?api_key=%s";
    }

    @Override
    protected String buildEndpointKey() {
        return "RIOT";
    }
}
