package grn.riot.lol;

import grn.database.QueryRow;
import grn.database.pojo.Match;
import grn.database.pojo.Player;
import grn.database.pojo.PlayerMatchStats;
import grn.database.pojo.Team;
import grn.database.repository.PlayerRepository;
import grn.database.repository.Repositories;
import grn.database.repository.Repository;
import grn.database.repository.TeamRepository;
import grn.database.service.MatchService;
import grn.endpoint.MatchesEndpoint;
import grn.endpoint.RequestResult;
import grn.error.ConsoleHandler;
import grn.exception.EndpointException;
import grn.exception.OutdatedApiKeyException;
import grn.properties.PropertiesHandler;
import grn.properties.json.JsonFileReader;
import grn.endpoint.MatchEndpoint;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MatchController implements Repository {

    public Match currentMatch;

    public void init () {

    }

    @Override
    public void reload() {

    }

    public List<Match> getAllMatches () {
        return new ArrayList<>();
    }

    public Match getCurrentMatch() {
        return currentMatch;
    }
}
