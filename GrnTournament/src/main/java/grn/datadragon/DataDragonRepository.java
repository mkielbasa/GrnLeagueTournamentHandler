package grn.datadragon;

import grn.datadragon.DataDragonHandler;
import grn.riot.lol.ChampionRepository;

public class DataDragonRepository {

    private ChampionRepository championRepository;

    public DataDragonRepository() {
        DataDragonHandler dDragonHandler = new DataDragonHandler();
        dDragonHandler.updateFiles();
        this.championRepository = new ChampionRepository(dDragonHandler.getFile(DataDragonHandler.CHAMPION));
    }

    public ChampionRepository getChampionRepository() {
        return championRepository;
    }
}
