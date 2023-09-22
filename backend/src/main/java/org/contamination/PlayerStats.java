package org.contamination;

import java.util.Objects;

public final class PlayerStats {

  private int numberOfInfectedPeople;
  private long survivalTime;

  public PlayerStats(int numberOfInfectedPeople, long survivalTime) {
    this.numberOfInfectedPeople = numberOfInfectedPeople;
    this.survivalTime = survivalTime;
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

  public void setSurvivalTime(long survivalTime) {
    this.survivalTime = survivalTime;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (PlayerStats) obj;
    return this.numberOfInfectedPeople == that.numberOfInfectedPeople &&
      this.survivalTime == that.survivalTime;
  }

  @Override
  public int hashCode() {
    return Objects.hash(numberOfInfectedPeople, survivalTime);
  }

  @Override
  public String toString() {
    return "PlayerStats[" +
      "numberOfInfectedPeople=" + numberOfInfectedPeople + ", " +
      "survivalTime=" + survivalTime + ']';
  }


}
