package org.contamination;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.websocket.Session;

import static org.contamination.CollisionDetector.anyCollision;

public class GameState {
  public static Map<Player, Session> PLAYERS = new ConcurrentHashMap<>();
  public static Map<String, Player> SESSION_IDS_PLAYERS = new ConcurrentHashMap<>();
  public static Map<Integer, PlayerInput> PLAYER_INPUTS = new ConcurrentHashMap<>();
  public static GameStatus GAME_STATUS = GameStatus.PENDING;
  public static AtomicInteger atomicInteger = new AtomicInteger();
  public static GameAwesomeStats gameStats = new GameAwesomeStats();
  public static long gameStartTime = 0;

  public static void addPlayer(Player player, Session session) {
    PLAYERS.put(player, session);
    SESSION_IDS_PLAYERS.put(session.getId(), player);
    PLAYER_INPUTS.put(player.getId(), new PlayerInput());
  }

  public static void removePlayer(Session session) {
    Player playerToRemove = SESSION_IDS_PLAYERS.get(session.getId());
    PLAYERS.remove(playerToRemove);
    SESSION_IDS_PLAYERS.remove(session.getId());
  }

  public static void start() {
    if (canStartTheGame()) {
      GAME_STATUS = GameStatus.RUNNING;
      positionPlayers();
      gameStartTime = System.currentTimeMillis();
      gameStats = new GameAwesomeStats();
    }
  }

  public static boolean canStartTheGame() {
    return numberOfPlayer() > 2;
  }

  private static int numberOfPlayer() {
    return PLAYERS.keySet().size();
  }

  public static void clean() {
    PLAYERS.keySet().forEach(Player::clean);
    gameStartTime = 0;
  }

  public static void positionPlayers() {
    for (Player player : PLAYERS.keySet()) {

      while (anyCollision(player)) {
        player.setX(Math.random());
        player.setY(Math.random());
      }

    }
  }

}
