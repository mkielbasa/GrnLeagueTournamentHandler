package grn.endpoint;

public class SummonerEndpoint extends EndpointRequest{

    private String summoner;

    public SummonerEndpoint (String summoner) {
        this.summoner = summoner;
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
        return "SUMMONER";
    }
}
