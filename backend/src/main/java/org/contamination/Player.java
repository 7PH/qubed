package org.contamination;

public class Player {
  public static final double MOVEMENT_FORCE = 1.5;

  public static final double FRICTION_SICK = 30;

  public static final double FRICTION = 3;

  /**
   * Player size in % of canvas width
   */
  public static final double SPRITE_SIZE = 0.015;

  private String name;
  private Integer id;
  private PlayerHealth health;
  private Long infectedAt;
  private PlayerStatus status;
  private double x;
  private double y;

  private PlayerStats playerStats;
  private double velX;
  private double velY;
  private double accX;
  private double accY;
  private double lastTickTime;

  public Player(String name) {
    this.name = name.length() > 20 ? name.substring(0, 20) : name;
    this.status = PlayerStatus.WAITING;
    this.health = PlayerHealth.HEALTHY;
    this.id = GameState.atomicInteger.getAndIncrement();
    this.x = Math.random();
    this.y = Math.random();
    this.playerStats = new PlayerStats(0, 0, 0);
    this.velX = 0;
    this.velY = 0;
    this.accX = 0;
    this.accY = 0;
    this.lastTickTime = System.currentTimeMillis();
  }

  public void clean() {
    this.x = Math.random();
    this.y = Math.random();
    this.velX = 0;
    this.velY = 0;
    this.accX = 0;
    this.accY = 0;
    this.lastTickTime = System.currentTimeMillis();
    this.health = PlayerHealth.HEALTHY;
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

  public PlayerHealth getHealth() {
    return health;
  }

  public void setHealth(PlayerHealth health) {
    this.health = health;
  }

  public Long getInfectedAt() {
    return infectedAt;
  }

  public void setInfectedAt(Long infectedAt) {
    this.infectedAt = infectedAt;
  }

  public void infect(Integer idOfInfectingPlayer) {
    if (health == PlayerHealth.HEALTHY) {
      health = PlayerHealth.INFECTED;
      infectedAt = System.currentTimeMillis();

      GameState.gameStats.onPlayerInfected(this.getId(), idOfInfectingPlayer);
    }
  }

  public boolean isSick() {
    return health == PlayerHealth.INFECTED || health == PlayerHealth.CONTAGIOUS;
  }

  public PlayerStats getPlayerStats() {
    return playerStats;
  }

  public void setPlayerStats(PlayerStats playerStats) {
    this.playerStats = playerStats;
  }

  public double getVelX() {
    return velX;
  }

  public void setVelX(double velX) {
    this.velX = velX;
  }

  public double getVelY() {
    return velY;
  }

  public void setVelY(double velY) {
    this.velY = velY;
  }

  public double getAccX() {
    return accX;
  }

  public void setAccX(double accX) {
    this.accX = accX;
  }

  public double getAccY() {
    return accY;
  }

  public void setAccY(double accY) {
    this.accY = accY;
  }

  public double getLastTickTime() {
    return lastTickTime;
  }

  public void setLastTickTime(double lastTickTime) {
    this.lastTickTime = lastTickTime;
  }

  public void applyForces(PlayerInput playerInput) {
    double delta = (System.currentTimeMillis() - this.getLastTickTime()) / 1000.0;
    this.setLastTickTime(System.currentTimeMillis());

    // Apply movement force to acceleration
    double angle = PlayerInput.getDirectionAngle(playerInput);
    this.setAccX(angle != -1 ? Math.cos(angle) * MOVEMENT_FORCE : 0);
    this.setAccY(angle != -1 ? Math.sin(angle) * MOVEMENT_FORCE : 0);

    // Apply friction to acceleration
    double friction = this.health == PlayerHealth.INFECTED ? FRICTION_SICK : FRICTION;
    this.setAccX(this.getAccX() - this.getVelX() * friction);
    this.setAccY(this.getAccY() - this.getVelY() * friction);

    // Apply acceleration to velocity
    this.setVelX(this.getVelX() + this.getAccX() * delta);
    this.setVelY(this.getVelY() + this.getAccY() * delta);

    // Apply velocity to position
    this.setX(this.getX() + this.getVelX() * delta);
    this.setY(this.getY() + this.getVelY() * delta);
  }

  public void repositionInBoundaries() {
    if (this.getX() < SPRITE_SIZE) {
      this.setX(SPRITE_SIZE);
      this.setVelX(Math.abs(this.getVelX()));
    }
    if (this.getX() > 1 - SPRITE_SIZE) {
      this.setX(1 - SPRITE_SIZE);
      this.setVelX(-Math.abs(this.getVelX()));
    }
    if (this.getY() < SPRITE_SIZE) {
      this.setY(SPRITE_SIZE);
      this.setVelY(Math.abs(this.getVelY()));
    }
    if (this.getY() > 1 - SPRITE_SIZE) {
      this.setY(1 - SPRITE_SIZE);
      this.setVelY(-Math.abs(this.getVelY()));
    }
  }
}
