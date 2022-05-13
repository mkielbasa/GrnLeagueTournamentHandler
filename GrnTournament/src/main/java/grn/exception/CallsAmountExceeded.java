package grn.exception;

public class CallsAmountExceeded extends EndpointException{

    public CallsAmountExceeded(String url) {
        super(url, "Calls amount exceeded!");
    }
}
