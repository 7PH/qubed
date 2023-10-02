package org.contamination;

import com.google.gson.Gson;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.contamination.CollisionDetector.getDistance;
import static org.contamination.CollisionDetector.getPlayerCollisions;
import static org.contamination.GameState.SPECTATORS;

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
        runBots();
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
    Stream.concat(GameState.PLAYERS.keySet().stream(), GameState.BOTS.stream())
        .filter(p -> p.getHealth() == PlayerHealth.INFECTED)
        .filter(GameLogic::shouldBecomeContagious)
        .forEach(p -> p.setHealth(PlayerHealth.CONTAGIOUS));

    long timeSinceStart = System.currentTimeMillis() - GameState.gameStats.gameStart;
    Stream.concat(GameState.PLAYERS.keySet().stream(), GameState.BOTS.stream())
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
    List<Player> players = Stream.concat(GameState.PLAYERS.keySet().stream(), GameState.BOTS.stream()).toList();
    Player player = players.get(new Random().nextInt(GameState.PLAYERS.keySet().size() + GameState.BOTS.size()));
    player.infect(null);
  }

  private static boolean isReadyToInfectTime() {
    return System.currentTimeMillis() - GameState.gameStartTime >= TIME_BEFORE_INFECTION;
  }

  private static boolean isZeroInfectedPlayer() {
    return Stream.concat(GameState.PLAYERS.keySet().stream(), GameState.BOTS.stream())
        .noneMatch(Player::isSick);
  }

  private long numberOfHealthyPlayers() {
    return Stream.concat(GameState.PLAYERS.keySet().stream(), GameState.BOTS.stream())
        .filter(player -> player.getHealth() == PlayerHealth.HEALTHY)
        .count();
  }

  private void calculateNewPositions() {
    for (Player player : GameState.PLAYERS.keySet()) {
      PlayerInput playerInput = GameState.PLAYER_INPUTS.get(player.getId());
      calculateNewPosition(player, playerInput);
    }
  }

  private void runBots() {
    for (Player bot : GameState.BOTS) {
      if (bot.getHealth() == PlayerHealth.HEALTHY) {
        runHealthyBot(bot);
      } else {
        runInfectedBot(bot);
      }
    }
  }

  private void runHealthyBot(Player bot) {
    List<Player> infected = Stream.concat(GameState.PLAYERS.keySet().stream(), GameState.BOTS.stream())
      .filter(player -> player.getHealth() != PlayerHealth.HEALTHY)
      .toList();
    Player closestInfected = getClosestPlayer(bot, infected);

    PlayerInput input = new PlayerInput();
    input.up = closestInfected != null && closestInfected.getY() > bot.getY() && bot.getY() > 0.1 ;
    input.down = closestInfected != null && closestInfected.getY() < bot.getY() && bot.getY() < 0.9;
    input.left = closestInfected != null && closestInfected.getX() > bot.getX() && bot.getX() > 0.1;
    input.right = closestInfected != null && closestInfected.getX() < bot.getX() && bot.getX() < 0.9;
    calculateNewPosition(bot, input);
  }

  private void runInfectedBot(Player bot) {
    List<Player> healthy = Stream.concat(GameState.PLAYERS.keySet().stream(), GameState.BOTS.stream())
      .filter(player -> player.getHealth() == PlayerHealth.HEALTHY)
      .toList();
    Player closestHealthy = getClosestPlayer(bot, healthy);

    PlayerInput input = new PlayerInput();
    input.up = closestHealthy != null && closestHealthy.getY() < bot.getY();
    input.down = closestHealthy != null && closestHealthy.getY() > bot.getY();
    input.left = closestHealthy != null && closestHealthy.getX() < bot.getX();
    input.right = closestHealthy != null && closestHealthy.getX() > bot.getX();
    calculateNewPosition(bot, input);
  }

  private Player getClosestPlayer(Player target, List<Player> list) {
    Player closestPlayer = null;
    double shortestDistance = 99999;

    for (Player p : list) {
      double d = getDistance(target.getX(), target.getY(), p.getX(), p.getY());
      if (d < shortestDistance) {
        shortestDistance = d;
        closestPlayer = p;
      }
    }

    return closestPlayer;
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
        Stream.concat(GameState.PLAYERS.keySet().stream(), GameState.BOTS.stream()).map(PlayerResponse::new).toList());
    String messageString = new Gson().toJson(new ReplyMessage("GAME_STATE", gameStateMessage));
    GameState.PLAYERS.values().forEach(
        s -> s.getAsyncRemote().sendText(messageString));
  }
}
