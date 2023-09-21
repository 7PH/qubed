package org.contamination;

public class Player {
  private String name;
  private String id;
  private boolean affected;
  private PlayerStatus status;
  private int x;
  private int y;

  public Player(String name) {
    this.name = name;
    this.status = PlayerStatus.WAITING;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean isAffected() {
    return affected;
  }

  public void setAffected(boolean affected) {
    this.affected = affected;
  }

  public PlayerStatus getStatus() {
    return status;
  }

  public void setStatus(PlayerStatus status) {
    this.status = status;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

}
