package org.contamination;

public class Player {
  private String name;
  private Integer id;
  private boolean infected;
  private PlayerStatus status;
  private double x;
  private double y;

  public Player(String name) {
    this.name = name;
    this.status = PlayerStatus.WAITING;
    this.id = GameState.atomicInteger.getAndIncrement();
    this.x = Math.random();
    this.y = Math.random();
  }

  public void clean() {
    this.x = Math.random();
    this.y = Math.random();
    this.infected = false;
    this.status = PlayerStatus.WAITING;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public boolean isInfected() {
    return infected;
  }

  public void setInfected(boolean infected) {
    this.infected = infected;
  }

  public PlayerStatus getStatus() {
    return status;
  }

  public void setStatus(PlayerStatus status) {
    this.status = status;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

}
