package grn.endpoint;

import grn.error.ConsoleHandler;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RequestResult {

    private final int code;
    private final String response;

    public RequestResult(int code, String response) {
        this.code = code;
        this.response = response;
    }

    public int getCode() {
        return code;
    }

    public String getResponse () {
        return response;
    }

    public Object parseJSON () {
        JSONParser parser = new JSONParser();
        try {
            return parser.parse(response);
        } catch (ParseException e) {
            ConsoleHandler.handleException(e);
        }
        return null;
    }
}
