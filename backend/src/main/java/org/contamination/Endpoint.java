package org.contamination;

import java.io.IOException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/websocket/")
public class Endpoint {

  @OnOpen
  public void onOpen(Session session) throws IOException {
    System.out.println(session.getId());
  }

  @OnMessage
  public void onMessage(Session session, String message) throws IOException {
    System.out.println(message);
  }

  @OnClose
  public void onClose(Session session) throws IOException {
    // WebSocket connection closes
  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    // Do error handling here
  }
}
