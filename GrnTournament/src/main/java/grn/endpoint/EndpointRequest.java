package grn.endpoint;

import grn.exception.BadRequestException;
import grn.exception.EndpointException;
import grn.exception.OutdatedApiKeyException;
import grn.http.HttpRequester;
import grn.properties.PropertiesHandler;

import java.io.IOException;

public abstract class EndpointRequest {

    public static final int CALLS_AMOUNT_EXCEEDED = 429;
    public static final int NOT_FOUND = 404;
    public static final int OUTDATED_API_KEY = 403;
    public static final int BAD_REQUEST = 400;

    protected String apiKey;
    protected String url;
    protected String[] params;

    protected String endpointKey;

    public EndpointRequest () {
        this.apiKey = PropertiesHandler.instance().getRiotApiKey();
        this.url = buildURL();
        this.params = buildParams();
        this.endpointKey = buildEndpointKey();
    }

    public RequestResult doRequest () throws EndpointException {
        RequestResult result = HttpRequester.doRequest(url, endpointKey, params);
        return result;
    }

    public String getEndpointKey() {
        return endpointKey;
    }

    abstract protected String[] buildParams ();

    abstract protected String buildURL ();

    abstract protected String buildEndpointKey ();
}
