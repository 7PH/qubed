package org.contamination;

public record PlayerResponse(String name, Integer id, PlayerHealth health, PlayerStatus status, double x, double y, PlayerStats playerStats) {

  public PlayerResponse(Player player) {
    this(player.getName(), player.getId(), player.getHealth(), player.getStatus(), player.getX(), player.getY(), player.getPlayerStats());
  }


}
