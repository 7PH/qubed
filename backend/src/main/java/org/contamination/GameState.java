package org.contamination;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.Session;

public class GameState {
  public static Map<Player, Session> PLAYERS = new ConcurrentHashMap<>();
  public static Map<String, Player> SESSION_IDS_PLAYERS = new ConcurrentHashMap<>();

  private static Map<String, PlayerInput> PLAYER_INPUTS = new ConcurrentHashMap<>();
  public static String GAME_STATUS;

  public static void addPlayer(Player player, Session session) {
    PLAYERS.put(player, session);
    SESSION_IDS_PLAYERS.put(session.getId(), player);
  }

  public static Optional<Player> getPlayer(String playerId) {
    return PLAYERS.keySet().stream()
      .filter(player -> player.getId().equals(playerId))
      .findFirst();
  }


}
