package grn.exception;

public class NotFoundException extends EndpointException{
    public NotFoundException(String url) {
        super(url, "Cannot find requested resource!");
    }
}
