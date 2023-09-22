package org.contamination;

import com.google.gson.Gson;
import java.util.List;
import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.contamination.CollisionDetector.getPlayerCollisions;

public class GameLogic implements Runnable {
  private static final double SPEED = 0.01;
  public static final double SIZE_OF_THE_SPRITE = 0.02;
  private static final long TIME_BEFORE_INFECTION = 5000;
  private static final long FRAME_RATE = 20;

  @Override
  public void run() {
    while (true) {
      if (GameState.GAME_STATUS == GameStatus.RUNNING) {
        if (numberOfNonInfectedPlayers() <= 1) {
          finishGame();
        }
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
    }

  }

  private static void finishGame() {
    GameState.GAME_STATUS = GameStatus.FINISHED;
    GameState.clean();
  }

  private static void randomlyInfectOnePlayer() {
    List<Player> players = GameState.PLAYERS.keySet().stream().toList();
    Player player = players.get(new Random().nextInt(GameState.PLAYERS.keySet().size()));
    player.setInfected(true);
    GameState.gameStats.onPlayerInfected(player.getId(), null);
  }

  private static boolean isReadyToInfectTime() {
    return System.currentTimeMillis() - GameState.gameStartTime >= TIME_BEFORE_INFECTION;
  }

  private static boolean isZeroInfectedPlayer() {
    return GameState.PLAYERS.keySet().stream()
      .noneMatch(Player::isInfected);
  }

  private long numberOfNonInfectedPlayers() {
    return GameState.PLAYERS.keySet().stream()
      .filter(player -> !player.isInfected())
      .count();
  }

  private void calculateNewPositions() {
    for (Player player : GameState.PLAYERS.keySet()) {
      PlayerInput playerInput = GameState.PLAYER_INPUTS.get(player.getId());
      calculateNewPosition(player, playerInput);
    }
  }

  private void calculateNewPosition(Player player, PlayerInput playerInput) {
    double oldX = player.getX();
    double oldY = player.getY();
    double step = SPEED;

    double x = (playerInput.right ? step : 0) + (playerInput.left ? -step : 0);
    double y = (playerInput.up ? -step : 0) + (playerInput.down ? step : 0);
    double newX = min(max(oldX + x, SIZE_OF_THE_SPRITE), 1 - SIZE_OF_THE_SPRITE);
    double newY = min(max(oldY + y, SIZE_OF_THE_SPRITE), 1 - SIZE_OF_THE_SPRITE);

    player.setY(newY);
    player.setX(newX);

    List<Player> playerCollisions = getPlayerCollisions(player);
    for (Player playerCollision : playerCollisions) {
      double D = Math.sqrt(Math.pow(playerCollision.getX() - player.getX(), 2) + Math.pow(playerCollision.getY() - player.getY(), 2));
      double d = (SIZE_OF_THE_SPRITE * 2) - D;
      double alpha = Math.atan2(playerCollision.getY() - player.getY(), playerCollision.getX() - player.getX());
      player.setX(player.getX() - Math.cos(alpha) * d);
      player.setY(player.getY() - Math.sin(alpha) * d);
      playerCollision.setX(playerCollision.getX() + Math.cos(alpha) * d);
      playerCollision.setY(playerCollision.getY() + Math.sin(alpha) * d);

      if (playerCollision.isInfected()) {
        player.setInfected(true);
        GameState.gameStats.onPlayerInfected(player.getId(), playerCollision.getId());
      } else if (player.isInfected()) {
        playerCollision.setInfected(true);
        GameState.gameStats.onPlayerInfected(playerCollision.getId(), player.getId());

      }
    }
  }

  public void sendState() {
    GameStateMessage gameStateMessage = new GameStateMessage(GameState.GAME_STATUS.name().toLowerCase(), GameState.PLAYERS.keySet().stream().toList());
    String messageString = new Gson().toJson(new ReplyMessage("GAME_STATE", gameStateMessage));
    GameState.PLAYERS.values().forEach(
      s -> s.getAsyncRemote().sendText(messageString)
    );
  }
}
