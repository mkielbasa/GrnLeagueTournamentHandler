package grn.riot.lol;

import grn.json.JsonParser;
import org.json.simple.JSONObject;

public class Champion implements JsonParser {

    private long id;
    private String name;
    private String icon;

    @Override
    public void parse(JSONObject jObject) {
        this.id = Long.parseLong(jObject.get("key").toString());
        this.name = (String)  jObject.get("name");
        JSONObject jImage = (JSONObject) jObject.get("image");
        this.icon = (String) jImage.get("full");
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

}
