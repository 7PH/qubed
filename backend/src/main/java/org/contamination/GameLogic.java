package org.contamination;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.contamination.CollisionDetector.getPlayerCollisions;

import java.util.List;
import java.util.Random;

import com.google.gson.Gson;

public class GameLogic implements Runnable {
  private static final double SPEED = 0.01;
  public static final double SIZE_OF_THE_SPRITE = 0.02;
  private static final long TIME_BEFORE_INFECTION = 5000;
  private static final long INCUBATION_PERIOD = 5000;
  private static final long FRAME_RATE = 20;

  @Override
  public void run() {
    while (true) {
      if (GameState.GAME_STATUS == GameStatus.RUNNING) {
        if (numberOfNonInfectedPlayers() <= 1) {
          finishGame();
        }
        updatePlayerHealth();
        calculateNewPositions();
        if (isZeroInfectedPlayer() && isReadyToInfectTime()) {
          randomlyInfectOnePlayer();
        }
      }

      try {
        Thread.sleep(FRAME_RATE);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      sendState();
      if (GameState.GAME_STATUS == GameStatus.FINISHED) {
        GameState.GAME_STATUS = GameStatus.PENDING;
      }
    }

  }

  private void updatePlayerHealth() {
    GameState.PLAYERS.keySet().stream()
      .filter(p -> p.getPlayerHealth() == PlayerHealth.INFECTED)
      .filter(GameLogic::shouldBecomeContagious)
      .forEach(p -> p.setPlayerHealth(PlayerHealth.CONTAGIOUS));
  }

  private static Boolean shouldBecomeContagious(Player player) {
    return System.currentTimeMillis() - player.getInfectedAt() > INCUBATION_PERIOD;
  }

  private static void finishGame() {
    GameState.GAME_STATUS = GameStatus.FINISHED;
    GameState.clean();
  }

  private static void randomlyInfectOnePlayer() {
    List<Player> players = GameState.PLAYERS.keySet().stream().toList();
    Player player = players.get(new Random().nextInt(GameState.PLAYERS.keySet().size()));
    player.setPlayerHealth(PlayerHealth.INFECTED);
    GameState.gameStats.onPlayerInfected(player.getId(), null);
  }

  private static boolean isReadyToInfectTime() {
    return System.currentTimeMillis() - GameState.gameStartTime >= TIME_BEFORE_INFECTION;
  }

  private static boolean isZeroInfectedPlayer() {
    return GameState.PLAYERS.keySet().stream()
        .noneMatch(Player::isSick);
  }

  private long numberOfNonInfectedPlayers() {
    return GameState.PLAYERS.keySet().stream()
        .filter(player -> !player.isSick())
        .count();
  }

  private void calculateNewPositions() {
    for (Player player : GameState.PLAYERS.keySet()) {
      PlayerInput playerInput = GameState.PLAYER_INPUTS.get(player.getId());
      calculateNewPosition(player, playerInput);
    }
  }

  /**
   * Get player direction angle based on its input. If no input is given, return
   * -1
   */
  private double getDirectionAngle(PlayerInput playerInput) {
    if (playerInput.right && playerInput.down) {
      return Math.PI / 4;
    } else if (playerInput.right && playerInput.up) {
      return 7 * Math.PI / 4;
    } else if (playerInput.left && playerInput.down) {
      return 3 * Math.PI / 4;
    } else if (playerInput.left && playerInput.up) {
      return 5 * Math.PI / 4;
    } else if (playerInput.right) {
      return 0;
    } else if (playerInput.down) {
      return Math.PI / 2;
    } else if (playerInput.left) {
      return Math.PI;
    } else if (playerInput.up) {
      return 3 * Math.PI / 2;
    } else {
      return -1;
    }
  }

  private void calculateNewPosition(Player player, PlayerInput playerInput) {
    double oldX = player.getX();
    double oldY = player.getY();
    double step =  player.getPlayerHealth() == PlayerHealth.INFECTED ? SPEED * 0.5 : SPEED;

    double angle = getDirectionAngle(playerInput);
    double xVel = angle != -1 ? Math.cos(angle) * step : 0;
    double yVel = angle != -1 ? Math.sin(angle) * step : 0;

    double newX = min(max(oldX + xVel, SIZE_OF_THE_SPRITE), 1 - SIZE_OF_THE_SPRITE);
    double newY = min(max(oldY + yVel, SIZE_OF_THE_SPRITE), 1 - SIZE_OF_THE_SPRITE);

    player.setY(newY);
    player.setX(newX);

    List<Player> playerCollisions = getPlayerCollisions(player);
    for (Player playerCollision : playerCollisions) {
      double D = Math.sqrt(
          Math.pow(playerCollision.getX() - player.getX(), 2) + Math.pow(playerCollision.getY() - player.getY(), 2));
      double d = (SIZE_OF_THE_SPRITE * 2) - D;
      double alpha = Math.atan2(playerCollision.getY() - player.getY(), playerCollision.getX() - player.getX());
      player.setX(player.getX() - Math.cos(alpha) * d);
      player.setY(player.getY() - Math.sin(alpha) * d);
      playerCollision.setX(playerCollision.getX() + Math.cos(alpha) * d);
      playerCollision.setY(playerCollision.getY() + Math.sin(alpha) * d);

      if (playerCollision.getPlayerHealth() == PlayerHealth.CONTAGIOUS) {
        player.infect();
        GameState.gameStats.onPlayerInfected(player.getId(), playerCollision.getId());
      } else if (player.getPlayerHealth() == PlayerHealth.CONTAGIOUS) {
        playerCollision.infect();
        GameState.gameStats.onPlayerInfected(playerCollision.getId(), player.getId());
      }
    }
  }

  public void sendState() {
    GameStateMessage gameStateMessage = new GameStateMessage(GameState.GAME_STATUS.name().toLowerCase(),
        GameState.PLAYERS.keySet().stream().toList());
    String messageString = new Gson().toJson(new ReplyMessage("GAME_STATE", gameStateMessage));
    GameState.PLAYERS.values().forEach(
        s -> s.getAsyncRemote().sendText(messageString));
  }
}
