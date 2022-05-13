package grn.endpoint;

import grn.error.ConsoleHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SummonerEndpoint extends EndpointRequest{

    private String summoner;

    public SummonerEndpoint (String summoner) {
        super();
        try {
            this.summoner = URLEncoder.encode(summoner, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            ConsoleHandler.handleException(e);
        }
        this.params = buildParams();
    }

    @Override
    protected String[] buildParams() {
        return new String[] {summoner, apiKey};
    }

    @Override
    protected String buildURL() {
        return "https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/%s?api_key=%s";
    }

    @Override
    protected String buildEndpointKey() {
        return "RIOT";
    }
}
