package grn.endpoint;

import grn.error.ConsoleHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SummonerEndpoint extends EndpointRequest{

    private String summoner;
    private String server;

    public SummonerEndpoint (String summoner, String server) {
        super();
        try {
            this.summoner = URLEncoder.encode(summoner, "UTF-8");
            this.server = server;
        } catch (UnsupportedEncodingException e) {
            ConsoleHandler.handleException(e);
        }
        this.params = buildParams();
    }

    @Override
    protected String[] buildParams() {
        return new String[] {server ,summoner, apiKey};
    }

    @Override
    protected String buildURL() {
        return "https://%s.api.riotgames.com/lol/summoner/v4/summoners/by-name/%s?api_key=%s";
    }

    @Override
    protected String buildEndpointKey() {
        return "RIOT";
    }
}
