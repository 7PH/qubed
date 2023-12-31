package org.contamination;

public final class PlayerStats {

  private int numberOfInfectedPeople;
  private long survivalTime;
  private long score;

  public PlayerStats(int numberOfInfectedPeople, long survivalTime, long score) {
    this.numberOfInfectedPeople = numberOfInfectedPeople;
    this.survivalTime = survivalTime;
    this.score = score;
    System.out.println("Resetting player scores :) ");
  }

  @Override
  public String toString() {
    return "PlayerStats{" +
      "numberOfInfectedPeople=" + numberOfInfectedPeople +
      ", survivalTime=" + survivalTime +
      ", score=" + score +
      '}';
  }

  public int numberOfInfectedPeople() {
    return numberOfInfectedPeople;
  }

  public long survivalTime() {
    return survivalTime;
  }

  public void setNumberOfInfectedPeople(int numberOfInfectedPeople) {
    this.numberOfInfectedPeople = numberOfInfectedPeople;
  }

  public void incrementNumberOfInfectingPlayer() {
    numberOfInfectedPeople++;
  }

  public void setSurvivalTime(long survivalTime) {
    this.survivalTime = survivalTime;
  }

  public long score() {
    return score;
  }

  public void setScore(long score) {
    this.score = score;
  }

}
