package grn.endpoint;

public class MatchEndpoint extends EndpointRequest{

    private String matchId;

    public MatchEndpoint (String matchId) {
        this.matchId = matchId;
    }

    @Override
    protected String[] buildParams() {
        return new String[] {matchId, apiKey};
    }

    @Override
    protected String buildURL() {
        return "https://europe.api.riotgames.com/lol/match/v5/matches/%s?api_key=%s";
    }
}
