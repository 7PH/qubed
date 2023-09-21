package org.contamination;

import com.google.gson.Gson;
import java.io.IOException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import static org.contamination.GameState.SESSION_IDS_PLAYERS;
import static org.contamination.GameState.addPlayer;
import static org.contamination.GameState.removePlayer;
import static org.contamination.PlayerStatus.READY;

@ServerEndpoint(value = "/websocket/{username}")
public class GameWebsocket {

  GameState gameState = new GameState();

  @OnOpen
  public void onOpen(Session session, @PathParam("username") String username) throws IOException {
    addPlayer(new Player(username), session);
  }

  @OnMessage
  public void onMessage(Session session, String message) throws IOException {

    Message msg = new Gson().fromJson(message, Message.class);
    switch (msg.type()) {
      case "ready":
        Player player = SESSION_IDS_PLAYERS.get(session.getId());
        player.setStatus(READY);
        break;
      case "start":
        GameState.start();
    }

  }

  @OnClose
  public void onClose(Session session) throws IOException {
    removePlayer(session);
  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    // Do error handling here
  }
}
