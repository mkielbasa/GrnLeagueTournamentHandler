package grn.http;

import grn.endpoint.RequestResult;
import grn.error.ConsoleHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class HttpRequester {

    public static RequestResult doRequest (String url, String... params) throws IOException {
        String formattedUrl = String.format(url, params);
        return HttpRequester.doRequest(formattedUrl);
    }

    private static RequestResult doRequest (String path) throws IOException {
        URL url = new URL(path);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");
        con.connect();
        int status = con.getResponseCode();
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

}
