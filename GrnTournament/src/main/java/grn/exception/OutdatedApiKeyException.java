package grn.exception;

public class OutdatedApiKeyException extends  Exception{

    public OutdatedApiKeyException () {
        super("Current Riot API key is outdated!");
    }
}
