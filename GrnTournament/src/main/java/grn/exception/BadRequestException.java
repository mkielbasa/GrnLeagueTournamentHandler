package grn.exception;

public class BadRequestException extends EndpointException{

    public BadRequestException (String url) {
        super(url, "Bad request!");
    }
}
