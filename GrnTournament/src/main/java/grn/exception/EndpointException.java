package grn.exception;

public class EndpointException extends  Exception {

    private String url;

    public EndpointException (String url, String message) {
        super(message + "(" + url + ")");
    }
}
