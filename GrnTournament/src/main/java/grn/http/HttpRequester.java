package grn.http;

import grn.endpoint.EndpointRequest;
import grn.endpoint.RequestResult;
import grn.error.ConsoleHandler;
import grn.exception.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static grn.endpoint.EndpointRequest.OUTDATED_API_KEY;

public class HttpRequester {

    private static final int ENDPOINT_LIMIT = 50;

    private static int endpointCalls = 0;

    public static void init () {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> resetKeyMinute(), 0, 60, TimeUnit.SECONDS);
    }

    private static void resetKeyMinute () {
        endpointCalls = readCalls();
        ConsoleHandler.handleInfo("Resetting key minutes...");
    }

    private static int readCalls () {
        int calls = 0;
        try {
            File file = new File("./GrnTournament/calls.log");
            if (!file.exists())
                return calls;
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            if ((line = br.readLine()) != null) {
                calls = Integer.parseInt(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return calls;
    }

    public static RequestResult doRequest (String url, String endpointKey, String... params) throws EndpointException {
        String formattedUrl = String.format(url, params);
        try {
            awaitTimers(endpointKey);
            return doRequest(formattedUrl);
        } catch (IOException e) {
            throw new OutdatedApiKeyException(formattedUrl);
        }
    }

    private static RequestResult doRequest (String path) throws IOException, EndpointException {
        URL url = new URL(path);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");
        con.connect();
        int status = con.getResponseCode();
        if (status == EndpointRequest.OUTDATED_API_KEY)
            throw new OutdatedApiKeyException(path);
        if (status == EndpointRequest.BAD_REQUEST)
            throw new BadRequestException(path);
        if (status == EndpointRequest.NOT_FOUND)
            throw new NotFoundException(path);
        if (status == EndpointRequest.CALLS_AMOUNT_EXCEEDED)
            throw new CallsAmountExceeded(path);
        ConsoleHandler.handleInfo("Request code " + status + " for " + path);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        return new RequestResult(status, content.toString());
    }

    private static void awaitTimers (String endpointKey) {
        try {
            if (endpointCalls >= ENDPOINT_LIMIT) {
                long seconds = 60;
                while (seconds > 0) {
                    if (seconds % 10 == 0)
                        ConsoleHandler.handleWarning("Exceeded enpoint " + endpointKey + " limit. Awaiting " + seconds + "...");
                    TimeUnit.SECONDS.sleep(1);
                    seconds--;
                }
                resetKeyMinute();
            } else {
                endpointCalls++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int getEndpointCalls() {
        return endpointCalls;
    }
}
