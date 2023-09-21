package org.contamination;

import java.util.List;

import static org.contamination.GameLogic.SIZE_OF_THE_SPRITE;
import static org.contamination.GameState.PLAYERS;

public class CollisionDetector {

  public static boolean anyCollision(Player player) {
    return PLAYERS.keySet().stream()
      .anyMatch(otherPlayer -> arePlayersColliding(player, otherPlayer));
  }

  public static List<Player> getPlayerCollisions(Player player) {
    return PLAYERS.keySet().stream()
      .filter(otherPlayer -> arePlayersColliding(player, otherPlayer))
      .toList();
  }

  public static boolean arePlayersColliding(Player player1, Player player2) {
    if (player1.getId().equals(player2.getId())) {
      return false;
    }
    return arePointsColliding(player1.getX(), player1.getY(), player2.getX(), player2.getY());
  }

  private static boolean arePointsColliding(double newX, double newY, double x1, double y1) {
    double distance = getDistance(newX, newY, x1, y1);
    return distance / 2 > SIZE_OF_THE_SPRITE;
  }

  private static double getDistance(double x1, double y1, double x2, double y2) {
    return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
  }

}
