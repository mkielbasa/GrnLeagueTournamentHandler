package grn.http;

import grn.endpoint.EndpointRequest;
import grn.endpoint.RequestResult;
import grn.error.ConsoleHandler;
import grn.exception.BadRequestException;
import grn.exception.EndpointException;
import grn.exception.OutdatedApiKeyException;
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

    private static long keyMinute;
    private static Map<String, Integer> endpointCalls = new HashMap<>();

    public static void init () {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> resetKeyMinute(), 0, 60, TimeUnit.SECONDS);
    }

    private static void resetKeyMinute () {
        keyMinute = System.currentTimeMillis();
        endpointCalls.clear();
        ConsoleHandler.handleInfo("Resetting key minutes...");
    }

    public static RequestResult doRequest (String url, String endpointKey, String... params) throws EndpointException {
        awaitTimers(endpointKey);
        String formattedUrl = String.format(url, params);
        try {
            return doRequest(formattedUrl);
        } catch (IOException e) {
            throw new OutdatedApiKeyException();
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
            throw new OutdatedApiKeyException();
        if (status == EndpointRequest.BAD_REQUEST)
            throw new BadRequestException();
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
            int calls = 0;
            if (endpointCalls.containsKey(endpointKey))
                calls = endpointCalls.get(endpointKey);
            if (calls >= ENDPOINT_LIMIT) {
                long timeLeft = System.currentTimeMillis() - keyMinute;
                long seconds = timeLeft / 1000;
                while (seconds > 0) {
                    ConsoleHandler.handleWarning("Exceeded enpoint " + endpointKey + " limit. Awaiting " + seconds + "...");
                    Thread.sleep(seconds * 1000);
                    seconds--;
                }
                resetKeyMinute();
            } else {
                calls++;
                endpointCalls.put(endpointKey, calls);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
