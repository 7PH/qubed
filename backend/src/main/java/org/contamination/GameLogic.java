package org.contamination;

import com.google.gson.Gson;

public class GameLogic implements Runnable {

  private static final double SPEED = 0.01;
  private static final double SIZE_OF_THE_SPRITE = 0.02;
  private static final long FRAME_RATE = 20;

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

    double newX = 0.0, newY = 0.0;

    if (playerInput.up) {
      newY = Math.min(oldY + step, 1);
    }
    if (playerInput.down) {
      newY = Math.min(oldY - step, 0);
    }
    if (playerInput.right) {
      newX = Math.min(oldX + step, 1);
    }
    if (playerInput.left) {
      newX = Math.min(oldX - step, 0);
    }

    for (Player otherPlayer : GameState.PLAYERS.keySet()) {
      if (otherPlayer.getId().equals(player.getId())) {
        continue;
      }

      double x1 = otherPlayer.getX();
      double y1 = otherPlayer.getY();

      boolean collisionDetected = arePointsColliding(newX, newY, x1, y1);

      if (collisionDetected) {
        return;
      }
    }

    player.setX(newX);
    player.setY(newY);

  }

  private boolean arePointsColliding(double newX, double newY, double x1, double y1) {
    double distance = getDistance(newX, newY, x1, y1);
    return distance / 2 > SIZE_OF_THE_SPRITE;
  }

  private double getDistance(double x1, double y1, double x2, double y2) {
    return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
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
