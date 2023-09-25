package org.contamination;

import static org.contamination.GameState.PLAYERS;
import static org.contamination.Player.SPRITE_SIZE;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollisionDetector {

  public static boolean anyCollision(Player player) {
    return Stream.concat(GameState.PLAYERS.keySet().stream(), GameState.BOTS.stream())
        .anyMatch(otherPlayer -> arePlayersColliding(player, otherPlayer));
  }

  public static List<Player> getPlayerCollisions(Player player) {
    return Stream.concat(GameState.PLAYERS.keySet().stream(), GameState.BOTS.stream())
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
    return distance / 2 < SPRITE_SIZE;
  }

  public static double getDistance(double x1, double y1, double x2, double y2) {
    return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
  }

}
