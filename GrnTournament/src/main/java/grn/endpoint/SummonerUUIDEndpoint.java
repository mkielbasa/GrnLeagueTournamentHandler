package grn.endpoint;

import grn.error.ConsoleHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SummonerUUIDEndpoint extends EndpointRequest{

    private String puuid;

    public SummonerUUIDEndpoint(String puuid) {
        super();
        try {
            this.puuid = URLEncoder.encode(puuid, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            ConsoleHandler.handleException(e);
        }
        this.params = buildParams();
    }

    @Override
    protected String[] buildParams() {
        return new String[] {puuid, apiKey};
    }

    @Override
    protected String buildURL() {
        return "https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/%s?api_key=%s";
    }

    @Override
    protected String buildEndpointKey() {
        return "RIOT";
    }
}
