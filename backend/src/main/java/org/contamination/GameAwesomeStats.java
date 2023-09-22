package org.contamination;

import java.util.HashMap;
import java.util.Map;

public class GameAwesomeStats {

  public final long gameStart;
  private Map<Integer, Long> survivalTimes = new HashMap<>();
  private Map<Integer, Integer> numberOfInfectedPeople = new HashMap<>();

  public GameAwesomeStats() {
    GameState.PLAYERS.forEach((p, s) -> {
      survivalTimes.put(p.getId(), 0L);
      numberOfInfectedPeople.put(p.getId(), 0);
    });

    gameStart = System.currentTimeMillis();
  }

  public void onPlayerInfected(Integer idOfInfectedPlayer, Integer idOfInfectingPlayer) {
    survivalTimes.put(idOfInfectedPlayer, System.currentTimeMillis() - gameStart);
    Player player = GameState.getPlayerById(idOfInfectedPlayer);
    player.getPlayerStats().setSurvivalTime(survivalTimes.get(player.getId()));

    if (idOfInfectingPlayer != null) {
      numberOfInfectedPeople.compute(idOfInfectingPlayer, (a, b) -> b + 1);
      player = GameState.getPlayerById(idOfInfectingPlayer);
      player.getPlayerStats().setNumberOfInfectedPeople(numberOfInfectedPeople.get(player.getId()));
    }

  }
}