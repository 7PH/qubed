package org.contamination;

import static java.lang.Math.ceil;

public class ScoreCalculator {

  public static void setPlayerScores() {
    GameState.PLAYERS.keySet().stream()
      .map(Player::getPlayerStats)
      .forEach(stat -> stat.setScore(calculatePlayerScore(stat)));
  }

  private static long calculatePlayerScore(PlayerStats stats) {
    int numberOfInfectedPeople = stats.numberOfInfectedPeople();
    long survivalTime = stats.survivalTime();
    return timeSurvivalFormula(survivalTime) + infectionFormula(numberOfInfectedPeople);
  }

  private static long infectionFormula(int numberOfInfectedPeople) {
    int totalPlayers = GameState.PLAYERS.size();
    int numerator = numberOfInfectedPeople * (numberOfInfectedPeople - 1);
    double denominator = (0.25 * totalPlayers) * (0.25 * totalPlayers - 1);
    return (long) ceil((numerator*100)/denominator);
  }

  private static long timeSurvivalFormula(long survivalTime) {
    int totalPlayers = GameState.PLAYERS.size();
    long scdLastSurvivor = GameState.PLAYERS.keySet().stream()
      .map(Player::getPlayerStats)
      .map(PlayerStats::survivalTime)
      .sorted()
      .toList()
      .get(totalPlayers - 2);
    return (survivalTime*100)/scdLastSurvivor;
  }
}
