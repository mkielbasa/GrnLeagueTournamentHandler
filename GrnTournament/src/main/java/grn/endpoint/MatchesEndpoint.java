package grn.endpoint;

public class MatchesEndpoint extends EndpointRequest{

    private String puuid;

    public MatchesEndpoint (String puuid) {
        this.puuid = puuid;
    }

    @Override
    protected String[] buildParams() {
        return new String[] {puuid, apiKey};
    }

    @Override
    protected String buildURL() {
        return "https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/%s/ids?start=0&count=20&api_key=%s";
    }

    @Override
    protected String buildEndpointKey() {
        return "MATCH";
    }
}
