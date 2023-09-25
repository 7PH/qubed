package org.contamination;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import javax.websocket.Session;

import static org.contamination.CollisionDetector.anyCollision;

public class GameState {
  public static Set<Player> BOTS = new HashSet<>();
  public static Map<Player, Session> PLAYERS = new ConcurrentHashMap<>();
  public static Map<Player, Session> SPECTATORS = new ConcurrentHashMap<>();
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

  public static void addSpectator(Player player, Session session) {
    SPECTATORS.put(player, session);
    SESSION_IDS_PLAYERS.put(session.getId(), player);
    PLAYER_INPUTS.put(player.getId(), new PlayerInput());
  }

  public static void removePlayer(Session session) {
    Player playerToRemove = SESSION_IDS_PLAYERS.get(session.getId());
    PLAYERS.remove(playerToRemove);
    SPECTATORS.remove(playerToRemove);
    SESSION_IDS_PLAYERS.remove(session.getId());
  }

  public static void start() {
    if (canStartTheGame()) {
      GAME_STATUS = GameStatus.RUNNING;
      makePlayersReady();
      positionPlayers(PLAYERS.keySet());
      positionPlayers(BOTS);
      gameStartTime = System.currentTimeMillis();
      gameStats = new GameAwesomeStats();
    }
  }

  private static void makePlayersReady() {
    PLAYERS.keySet().forEach(p -> p.setStatus(PlayerStatus.READY));
  }

  public static void addBot() {
    Integer index = BOTS.size() + 1;
    if (index < 10) {
      Player newBot = new Player("bot " + index);
      newBot.setStatus(PlayerStatus.READY);
      BOTS.add(newBot);
    }
  }

  public static void removeBot() {
    if (BOTS.size() > 0) {
      Player lastBot = BOTS.stream().toList().get(BOTS.size() - 1);
      BOTS.remove(lastBot);
    }
  }

  public static boolean canStartTheGame() {
    return numberOfPlayer() >= 1;
  }

  private static int numberOfPlayer() {
    return PLAYERS.keySet().size();
  }

  public static void clean() {
    PLAYERS.keySet().forEach(Player::clean);
    BOTS.forEach(Player::clean);
    gameStartTime = 0;
  }

  public static void positionPlayers(Set<Player> players) {
    for (Player player : players) {

      while (anyCollision(player)) {
        player.setX(Math.random());
        player.setY(Math.random());
      }

    }
  }

  public static Player getPlayerById(Integer id) {
    return Stream.concat(GameState.PLAYERS.keySet().stream(), GameState.BOTS.stream()).filter(p -> p.getId().equals(id)).findFirst().get();
  }

}
