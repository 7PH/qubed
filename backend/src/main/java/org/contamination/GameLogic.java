package org.contamination;

import com.google.gson.Gson;
import java.util.List;
import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static javax.swing.UIManager.get;
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
    double newX = min(max(oldX+x, 0), 1);
    double newY = min(max(oldY+y, 0), 1);

    player.setY(newY);
    player.setX(newX);

    List<Player> playerCollisions = getPlayerCollisions(player);
    for (Player playerCollision : playerCollisions) {
      //compute vector angle between the two Player
      double alpha = Math.atan2(playerCollision.getY() - player.getY(), playerCollision.getX() - player.getX());
      //reposition player to the edge of the other player
      player.setX(playerCollision.getX() - Math.cos(alpha) * SIZE_OF_THE_SPRITE);
      player.setY(playerCollision.getY() - Math.sin(alpha) * SIZE_OF_THE_SPRITE);
    }

    if (playerCollisions.stream().anyMatch(Player::isInfected)) {
      player.setInfected(true);
      playerCollisions.forEach(otherPlayer -> otherPlayer.setInfected(true));
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
