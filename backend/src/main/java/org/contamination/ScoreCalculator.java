package org.contamination;

import static java.lang.Math.ceil;

public class ScoreCalculator {

  public static void setPlayerScores() {
    GameState.PLAYERS.keySet().stream()
      .peek(player -> System.out.println("Calculating score for " + player.getName()))
      .map(Player::getPlayerStats)
      .forEach(stat -> stat.setScore(calculatePlayerScore(stat)));
  }

  private static long calculatePlayerScore(PlayerStats stats) {
    int numberOfInfectedPeople = stats.numberOfInfectedPeople();
    long survivalTime = stats.survivalTime();
    long timeSurvivalFormula = timeSurvivalFormula(survivalTime);
    System.out.println("timeSurvivalFormula="+timeSurvivalFormula);
    long infectionFormula = infectionFormula(numberOfInfectedPeople);
    System.out.println("infectionFormula="+infectionFormula);
    long score = timeSurvivalFormula + infectionFormula;
    System.out.println("Score="+score);
    return score;
  }

  private static long infectionFormula(int numberOfInfectedPeople) {
    if (numberOfInfectedPeople == 0) {
      return 0;
    }

    int totalPlayers = GameState.PLAYERS.size();
    double baseLine = ceil(0.3 * totalPlayers);
    double denominator = Math.max(((baseLine) * (baseLine - 1)), 3);
    if (numberOfInfectedPeople == 1) {
      return (long)ceil(100/denominator);
    }

    int numerator = numberOfInfectedPeople * (numberOfInfectedPeople - 1);
    return (long)ceil((numerator*100)/denominator);
  }

  private static long timeSurvivalFormula(long survivalTime) {
    int totalPlayers = GameState.PLAYERS.size();
    long scdLastSurvivor = GameState.PLAYERS.keySet().stream()
      .map(Player::getPlayerStats)
      .map(PlayerStats::survivalTime)
      .sorted()
      .toList()
      .get(totalPlayers - 2);
    long pointsByScd = survivalTime / 1000;
    return (long) ceil((1.5 * (survivalTime*100)/scdLastSurvivor) + pointsByScd);
  }
}
