package grn.datadragon;

import grn.file.FileDownloader;
import grn.properties.PropertiesHandler;

public class DataDragonHandler {
    private static final String[] DDRAGON_FILES = {"champion.json", "item.json", "profileicon.json", "runesReforged.json", "summoner.json"};

    public void updateFiles () {
        String dataDragonUrl = PropertiesHandler.instance().getDataDragonUrl();
        String dataDragonFileDirectory = PropertiesHandler.instance().getDataFileDirectory();
        for (String file : DDRAGON_FILES) {
            String url = dataDragonUrl + file;
            String savePath = dataDragonFileDirectory + "/" + file;
            FileDownloader.download(url, savePath);
        }
    }
}
