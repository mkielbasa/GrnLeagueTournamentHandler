package grn.datadragon;

import grn.http.file.FileDownloader;
import grn.properties.PropertiesHandler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DataDragonHandler {
    public static final String CHAMPION = "champion.json";
    public static final String ITEM = "item.json";
    public static final String PROFILE_ICON = "profileicon.json";
    public static final String RUNES = "runesReforged.json";
    public static final String SUMMONER = "summoner.json";
    public static final String[] DDRAGON_FILES = {CHAMPION, ITEM, PROFILE_ICON, RUNES, SUMMONER};

    private Map<String, File> cache = new HashMap<>();

    public void updateFiles () {
        String dataDragonUrl = PropertiesHandler.instance().getDataDragonUrl();
        String dataDragonFileDirectory = PropertiesHandler.instance().getDataFileDirectory();
        for (String file : DDRAGON_FILES) {
            String url = dataDragonUrl + file;
            String savePath = dataDragonFileDirectory + "/" + file;
            File downloadedFile = FileDownloader.download(url, savePath);
            cache.put(file, downloadedFile);
        }
    }

    public File getFile (String file) {
        return cache.get(file);
    }
}
