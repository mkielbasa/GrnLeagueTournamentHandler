package grn.exception;

public class BadRequestException extends EndpointException{

    public BadRequestException () {
        super("Bad request!");
    }
}
