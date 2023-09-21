package org.contamination;

import com.google.gson.Gson;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameLogic implements Runnable {

  private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

  @Override
  public void run() {
    //here is where we write the code.
    while (true) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      sendState();
    }
  }


  public void sendState() {
    GameStateMessage gameStateMessage = new GameStateMessage(GameState.GAME_STATUS, GameState.PLAYERS.keySet().stream().toList());
    String messageString = new Gson().toJson(gameStateMessage);
    System.out.println(messageString);
    GameState.PLAYERS.values().forEach(
      s -> s.getAsyncRemote().sendText(messageString)
    );
  }
}
