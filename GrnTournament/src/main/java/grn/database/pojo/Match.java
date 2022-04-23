package grn.database.pojo;


import com.example.grntournament.GrnTournamentApplication;
import grn.database.QueryRow;
import grn.database.repository.TeamRepository;
import grn.database.service.MatchService;
import grn.properties.PropertiesHandler;

public class Match {

  private long id;
  private long teamA;
  private long teamB;
  private boolean finished;
  private int queue;
  private String matchId;
  private int participants;
  private Team teamAObject;
  private Team teamBObject;
  private String time;
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
    this.queue = (int) row.get(5);
    this.matchId = (String) row.get(6);
    this.participants = (int) row.get(7);

    TeamRepository teamRepository = GrnTournamentApplication.getTeamRepository();
    this.teamAObject = teamRepository.getTeam(teamA);
    this.teamBObject = teamRepository.getTeam(teamB);
    this.teamAName = teamAObject.getName();
    this.teamAShortName = teamAObject.getShortName();
    this.teamAIcon = teamAObject.getIcon();
    this.teamBName = teamBObject.getName();
    this.teamBShortName = teamBObject.getShortName();
    this.teamBIcon = teamBObject.getIcon();
    this.time = buildTime();
    this.result = "vs";
    if (finished) {
      this.result = MatchService.getMatchResult(id, teamA, teamB);
    }
  }

  private String buildTime () {
    int startTime = PropertiesHandler.instance().getTournamentStartHour();
    int matchTime = PropertiesHandler.instance().getMatchTime();
    int minutesOffset = (queue - 1) * matchTime;
    int hours = startTime;
    int minutes = minutesOffset;
    if (minutesOffset >= 60) {
      hours = (minutesOffset / 60) + startTime;
      minutes = minutesOffset % 60;
    }
    return "" + (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes);
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

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
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


  public int getQueue() {
    return queue;
  }

  public void setQueue(int queue) {
    this.queue = queue;
  }


  public String getMatchId() {
    return matchId;
  }

  public void setMatchId(String matchId) {
    this.matchId = matchId;
  }

  public int getParticipants() {
    return participants;
  }

  public void setParticipants(int participants) {
    this.participants = participants;
  }
}
