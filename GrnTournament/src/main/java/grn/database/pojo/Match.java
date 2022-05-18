package grn.database.pojo;


import com.example.grntournament.GrnTournamentApplication;
import grn.database.QueryRow;
import grn.database.repository.Repositories;
import grn.database.repository.TeamRepository;
import grn.database.service.MatchService;
import grn.properties.PropertiesHandler;

public class Match {

  private long id;
  private long teamA;
  private long teamB;
  private boolean finished;
  private long parentId;
  private long winner;
  private int teamAScore;
  private int teamBScore;

  private Team teamAObject;
  private Team teamBObject;
  private String teamAName;
  private String teamBName;
  private String teamAShortName;
  private String teamBShortName;
  private String teamAIcon;
  private String teamBIcon;
  private String result;

  public void fromQueryRow (QueryRow row) {
    this.id = (long) row.get(1);
    this.teamA = (long) row.get(2);
    this.teamB = (long) row.get(3);
    this.finished = (boolean) row.get(4);
    this.parentId = (long) row.get(5);
    this.winner = (long) row.get(6);
    this.teamAScore = (int) row.get(7);
    this.teamBScore = (int) row.get(8);

    TeamRepository teamRepository = Repositories.getTeamRepository();
    this.teamAObject = teamRepository.getTeam(teamA);
    this.teamBObject = teamRepository.getTeam(teamB);
    this.teamAName = teamAObject.getName();
    this.teamAShortName = teamAObject.getShortName();
    this.teamAIcon = teamAObject.getIcon();
    this.teamBName = teamBObject.getName();
    this.teamBShortName = teamBObject.getShortName();
    this.teamBIcon = teamBObject.getIcon();
    this.result = "vs";
  }

  public int getTeamAScore() {
    return teamAScore;
  }

  public int getTeamBScore() {
    return teamBScore;
  }

  public Team getTeamAObject() {
    return teamAObject;
  }

  public void setTeamAObject(Team teamAObject) {
    this.teamAObject = teamAObject;
  }

  public Team getTeamBObject() {
    return teamBObject;
  }

  public void setTeamBObject(Team teamBObject) {
    this.teamBObject = teamBObject;
  }

  public String getTeamAName() {
    return teamAName;
  }

  public String getTeamBName() {
    return teamBName;
  }

  public String getTeamAShortName() {
    return teamAShortName;
  }

  public String getTeamBShortName() {
    return teamBShortName;
  }

  public String getTeamAIcon() {
    return teamAIcon;
  }

  public String getTeamBIcon() {
    return teamBIcon;
  }

  public String getResult() {
    return result;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getTeamA() {
    return teamA;
  }

  public void setTeamA(long teamA) {
    this.teamA = teamA;
  }

  public long getTeamB() {
    return teamB;
  }

  public void setTeamB(long teamB) {
    this.teamB = teamB;
  }

  public boolean isFinished() {
    return finished;
  }

  public void setFinished(boolean finished) {
    this.finished = finished;
  }

}
