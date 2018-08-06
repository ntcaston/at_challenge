package com.caston.challenge.demo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Basic HTTP handler for demoing the project which returns a very basic static
 * message to all requests.
 *
 * Depends on Oracle JRE for HTTP library.
 */
public class DemoHttpHandler implements HttpHandler {
  /**
   * Content sent in body of HTTP requests.
   *
   * Doesn't need to be anything fancy, just enough to verify things are working
   * as expected.
   */
  private static final String DEMO_RESPONSE = "Welcome to the demo page!";
  private static final int HTTP_STATUS_OK = 200;

  /**
   * Creates an DemoHttpHandler instance on the specified path and port and
   * starts the server immediately.
   *
   * This call blocks the calling thread.
   */
  public static void blockingCreateAndStart(String path, int port) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(port),
        /* backlog, use default */0);
    server.createContext(path, new DemoHttpHandler());
    server.start();
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    exchange.sendResponseHeaders(HTTP_STATUS_OK, DEMO_RESPONSE.length());
    OutputStream outStream = exchange.getResponseBody();
    outStream.write(DEMO_RESPONSE.getBytes());
    outStream.close();
  }
}