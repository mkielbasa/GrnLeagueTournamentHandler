package grn.riot.lol;

import grn.datadragon.DataDragonHandler;
import grn.json.JsonFileReader;

import java.io.File;

public class MetadataRepository {

    private ChampionRepository championRepository;

    public MetadataRepository () {
        DataDragonHandler dDragonHandler = new DataDragonHandler();
        dDragonHandler.updateFiles();
        this.championRepository = new ChampionRepository(dDragonHandler.getFile(DataDragonHandler.CHAMPION));
    }
}
