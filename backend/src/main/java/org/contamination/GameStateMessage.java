package org.contamination;

import java.util.List;

public record GameStateMessage(String gameState, List<Player> players) {
}
