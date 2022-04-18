package grn.http;

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

    public static Object doRequest (String url, String... params) {
        String formattedUrl = String.format(url, params);
        String response = HttpRequester.doRequest(formattedUrl);
        JSONParser parser = new JSONParser();
        try {
            return parser.parse(response);
        } catch (ParseException e) {
            ConsoleHandler.handleException(e);
        }
        return null;
    }

    private static String doRequest (String path) {
        try {
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
            ConsoleHandler.handleInfo("Output: " + content.toString());
            con.disconnect();
            return content.toString();
        } catch (ProtocolException e) {
            ConsoleHandler.handleException(e);
        } catch (MalformedURLException e) {
            ConsoleHandler.handleException(e);
        } catch (IOException e) {
            ConsoleHandler.handleException(e);
        }
        return null;
    }

}
