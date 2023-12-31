package org.contamination;

import static org.contamination.CollisionDetector.getPlayerCollisions;
import static org.contamination.GameState.PLAYER_INPUTS;
import static org.contamination.GameState.SPECTATORS;

import java.util.List;
import java.util.Random;

import com.google.gson.Gson;

public class GameLogic implements Runnable {
  private static final long TIME_BEFORE_INFECTION = 7 * 1000;
  private static final long INCUBATION_PERIOD = 3000;
  private static final long FRAME_RATE = 20;

  @Override
  public void run() {
    while (true) {
      if (GameState.GAME_STATUS == GameStatus.RUNNING) {
        if (numberOfHealthyPlayers() == 0) {
          finishGame();
          continue;
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
        GameState.PLAYERS.putAll(SPECTATORS);
        SPECTATORS.clear();
      }
    }

  }

  private void updatePlayerHealth() {
    GameState.PLAYERS.keySet().stream()
        .filter(p -> p.getHealth() == PlayerHealth.INFECTED)
        .filter(GameLogic::shouldBecomeContagious)
        .forEach(p -> p.setHealth(PlayerHealth.CONTAGIOUS));

    long timeSinceStart = System.currentTimeMillis() - GameState.gameStats.gameStart;
    GameState.PLAYERS.keySet().stream()
        .filter(p -> p.getHealth() == PlayerHealth.HEALTHY)
        .forEach(p -> {
          p.getPlayerStats().setSurvivalTime(timeSinceStart);
        });
  }

  private static Boolean shouldBecomeContagious(Player player) {
    return System.currentTimeMillis() - player.getInfectedAt() > INCUBATION_PERIOD;
  }

  private static void finishGame() {
    GameState.GAME_STATUS = GameStatus.FINISHED;
    ScoreCalculator.setPlayerScores();
    GameState.clean();
  }

  private static void randomlyInfectOnePlayer() {
    List<Player> players = GameState.PLAYERS.keySet().stream().toList();
    Player player = players.get(new Random().nextInt(GameState.PLAYERS.keySet().size()));
    player.infect(null);
  }

  private static boolean isReadyToInfectTime() {
    return System.currentTimeMillis() - GameState.gameStartTime >= TIME_BEFORE_INFECTION;
  }

  private static boolean isZeroInfectedPlayer() {
    return GameState.PLAYERS.keySet().stream()
        .noneMatch(Player::isSick);
  }

  private long numberOfHealthyPlayers() {
    return GameState.PLAYERS.keySet().stream()
        .filter(player -> player.getHealth() == PlayerHealth.HEALTHY)
        .count();
  }

  private void calculateNewPositions() {
    for (Player player : GameState.PLAYERS.keySet()) {
      PlayerInput playerInput = GameState.PLAYER_INPUTS.get(player.getId());
      calculateNewPosition(player, playerInput);
    }
  }

  private void calculateNewPosition(Player player, PlayerInput playerInput) {
    player.applyForces(playerInput);
    player.repositionInBoundaries();

    List<Player> playerCollisions = getPlayerCollisions(player);
    for (Player playerCollision : playerCollisions) {
      double D = Math.sqrt(
          Math.pow(playerCollision.getX() - player.getX(), 2) + Math.pow(playerCollision.getY() - player.getY(), 2));
      double d = (Player.SPRITE_SIZE * 2) - D;
      double alpha = Math.atan2(playerCollision.getY() - player.getY(), playerCollision.getX() - player.getX());
      player.setX(player.getX() - Math.cos(alpha) * d);
      player.setY(player.getY() - Math.sin(alpha) * d);
      playerCollision.setX(playerCollision.getX() + Math.cos(alpha) * d);
      playerCollision.setY(playerCollision.getY() + Math.sin(alpha) * d);

      if (playerCollision.getHealth() == PlayerHealth.CONTAGIOUS) {
        player.infect(playerCollision.getId());
      } else if (player.getHealth() == PlayerHealth.CONTAGIOUS) {
        playerCollision.infect(player.getId());
      }
    }
  }

  public void sendState() {
    GameStateMessage gameStateMessage = new GameStateMessage(GameState.GAME_STATUS.name().toLowerCase(),
        GameState.PLAYERS.keySet().stream().map(PlayerResponse::new).toList());
    String messageString = new Gson().toJson(new ReplyMessage("GAME_STATE", gameStateMessage));
    GameState.PLAYERS.values().forEach(
        s -> s.getAsyncRemote().sendText(messageString));
  }
}
