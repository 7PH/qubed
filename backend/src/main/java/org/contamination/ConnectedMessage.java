package org.contamination;

public record ConnectedMessage(String type, Integer id) {

  public ConnectedMessage(Integer id) {
    this("CONNECTED", id);
  }
}
