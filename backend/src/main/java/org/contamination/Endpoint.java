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
    // Get session and WebSocket connection
  }

  @OnMessage
  public void onMessage(Session session, String message) throws IOException {
    // Handle new messages
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
