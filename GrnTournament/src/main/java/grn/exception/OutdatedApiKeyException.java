package grn.exception;

public class OutdatedApiKeyException extends EndpointException{

    public OutdatedApiKeyException (String url) {
        super(url, "Current Riot API key is outdated!");
    }
}
