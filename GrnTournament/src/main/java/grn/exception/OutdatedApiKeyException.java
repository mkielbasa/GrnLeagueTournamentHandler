package grn.exception;

public class OutdatedApiKeyException extends EndpointException{

    public OutdatedApiKeyException () {
        super("Current Riot API key is outdated!");
    }
}
