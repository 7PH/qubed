package org.contamination;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.javax.server.config.JavaxWebSocketServletContainerInitializer;

public class HelloWorld {
  public static void main(String[] args) throws Exception {

    Server server = new Server(8080);
    ServerConnector connector = new ServerConnector(server);
    server.addConnector(connector);
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);

    JavaxWebSocketServletContainerInitializer.configure(context, (servletContext, wsContainer) ->
    {
      // This lambda will be called at the appropriate place in the
      // ServletContext initialization phase where you can initialize
      // and configure  your websocket container.

      // Configure defaults for container
      wsContainer.setDefaultMaxTextMessageBufferSize(65535);

      // Add WebSocket endpoint to javax.websocket layer
      wsContainer.addEndpoint(Endpoint.class);
    });
    server.start();
    createNewThread();
    server.join();
  }

  private static void createNewThread() {
//    new Thread(runnable).setDaemon(true);
  }
}
