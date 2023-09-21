package org.contamination;

import java.util.List;

public record GameStateMessage(String type, String gameState, List<Player> players) {

  public GameStateMessage(String gameState, List<Player> players) {
    this("GAME_STATE", gameState, players);
  }
}
