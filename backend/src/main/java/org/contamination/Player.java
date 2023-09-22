package org.contamination;

public class Player {
  private String name;
  private Integer id;
  private PlayerHealth playerHealth;
  private Long infectedAt;
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
    this.playerHealth = PlayerHealth.HEALTHY;
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

  public PlayerHealth getPlayerHealth() {
    return playerHealth;
  }

  public void setPlayerHealth(PlayerHealth playerHealth) {
    this.playerHealth = playerHealth;
  }

  public Long getInfectedAt() {
    return infectedAt;
  }

  public void setInfectedAt(Long infectedAt) {
    this.infectedAt = infectedAt;
  }

  public void infect() {
    if (playerHealth == PlayerHealth.HEALTHY) {
      playerHealth = PlayerHealth.INFECTED;
      infectedAt = System.currentTimeMillis();
    }
  }

  public boolean isSick() {
    return playerHealth == PlayerHealth.INFECTED || playerHealth == PlayerHealth.CONTAGIOUS;
  }
}
