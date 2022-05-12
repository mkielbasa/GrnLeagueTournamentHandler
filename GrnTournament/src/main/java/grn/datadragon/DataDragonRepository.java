package grn.datadragon;

import grn.database.repository.Repository;
import grn.riot.lol.ChampionRepository;

public class DataDragonRepository implements Repository {

    private ChampionRepository championRepository;

    @Override
    public void init() {
        DataDragonHandler dDragonHandler = new DataDragonHandler();
        dDragonHandler.updateFiles();
        this.championRepository = new ChampionRepository(dDragonHandler.getFile(DataDragonHandler.CHAMPION));
    }

    @Override
    public void reload() {

    }

    public ChampionRepository getChampionRepository() {
        return championRepository;
    }

}
