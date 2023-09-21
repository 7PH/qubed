package org.contamination;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.websocket.Session;

public class GameState {
  public static Map<Player, Session> PLAYERS = new ConcurrentHashMap<>();
  public static Map<String, Player> SESSION_IDS_PLAYERS = new ConcurrentHashMap<>();
  public static Map<Integer, PlayerInput> PLAYER_INPUTS = new ConcurrentHashMap<>();
  public static GameStatus GAME_STATUS = GameStatus.PENDING;
  public static AtomicInteger atomicInteger = new AtomicInteger();

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
    GAME_STATUS = GameStatus.RUNNING;
  }
}
