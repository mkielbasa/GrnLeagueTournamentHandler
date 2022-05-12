package grn.endpoint;

import grn.exception.OutdatedApiKeyException;
import grn.http.HttpRequester;
import grn.properties.PropertiesHandler;

import java.io.IOException;

public abstract class EndpointRequest {

    private static final int OUTDATED_API_KEY = 403;

    protected String apiKey;
    private String url;
    private String[] params;

    public EndpointRequest () {
        this.apiKey = PropertiesHandler.instance().getRiotApiKey();
        this.url = buildURL();
        this.params = buildParams();
    }

    public RequestResult doRequest () throws OutdatedApiKeyException {
        try {
            RequestResult result = HttpRequester.doRequest(url, params);
            if (result.getCode() == OUTDATED_API_KEY)
                throw new OutdatedApiKeyException();
            return result;
        } catch (IOException e) {
            throw new OutdatedApiKeyException();
        }
    }

    abstract protected String[] buildParams ();

    abstract protected String buildURL ();
}
