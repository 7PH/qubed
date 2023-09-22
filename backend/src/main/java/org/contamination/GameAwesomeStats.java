package org.contamination;

public class GameAwesomeStats {

  public final long gameStart;

  public GameAwesomeStats() {
    GameState.PLAYERS.forEach((p, s) -> {
      p.setPlayerStats(new PlayerStats(0, 0));
    });

    gameStart = System.currentTimeMillis();
  }

  public void onPlayerInfected(Integer idOfInfectedPlayer, Integer idOfInfectingPlayer) {
    Player playerById = GameState.getPlayerById(idOfInfectedPlayer);
    playerById.getPlayerStats().setSurvivalTime(System.currentTimeMillis() - gameStart);

    if (idOfInfectingPlayer != null) {
      Player playerInfecting = GameState.getPlayerById(idOfInfectingPlayer);
      playerInfecting.getPlayerStats().incrementNumberOfInfectingPlayer();
    }

  }
}
