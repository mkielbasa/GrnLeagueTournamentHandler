package grn.database.pojo;


public class Player {

  private long id;
  private String accountId;
  private String riotId;
  private String pUuid;
  private String name;
  private long teamId;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }


  public String getRiotId() {
    return riotId;
  }

  public void setRiotId(String riotId) {
    this.riotId = riotId;
  }


  public String getPUuid() {
    return pUuid;
  }

  public void setPUuid(String pUuid) {
    this.pUuid = pUuid;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public long getTeamId() {
    return teamId;
  }

  public void setTeamId(long teamId) {
    this.teamId = teamId;
  }

}
