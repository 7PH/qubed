package org.contamination;

import com.google.gson.Gson;
import java.util.List;

import static org.contamination.CollisionDetector.getPlayerCollisions;

public class GameLogic implements Runnable {

  private static final double SPEED = 0.01;
  public static final double SIZE_OF_THE_SPRITE = 0.02;
  private static final long FRAME_RATE = 1000;

  @Override
  public void run() {

    while (true) {
      if (GameState.GAME_STATUS == GameStatus.RUNNING) {
        calculateNewPositions();
      }
      try {
        Thread.sleep(FRAME_RATE);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      sendState();

    }

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

    double newX = oldX, newY = oldY;

    if (playerInput.up) {
      newY = Math.min(oldY + step, 1);
    }
    if (playerInput.down) {
      newY = Math.max(oldY - step, 0);
    }
    if (playerInput.right) {
      newX = Math.min(oldX + step, 1);
    }
    if (playerInput.left) {
      newX = Math.max(oldX - step, 0);
    }

    List<Player> playerCollisions = getPlayerCollisions(player);

    if (playerCollisions.stream().anyMatch(Player::isInfected)) {
      player.setInfected(true);
    }

    if (playerCollisions.isEmpty()) {
      player.setY(newY);
      player.setX(newX);
    }

  }

  public void sendState() {
    GameStateMessage gameStateMessage = new GameStateMessage(GameState.GAME_STATUS.name().toLowerCase(), GameState.PLAYERS.keySet().stream().toList());
    String messageString = new Gson().toJson(new ReplyMessage("GAME_STATE", gameStateMessage));
    System.out.println(messageString);
    GameState.PLAYERS.values().forEach(
      s -> s.getAsyncRemote().sendText(messageString)
    );
  }
}
