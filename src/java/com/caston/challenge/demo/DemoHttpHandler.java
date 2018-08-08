package com.caston.challenge.demo;

import com.caston.challenge.server.InetSocketHostExtractor;
import com.caston.challenge.server.NullRateLimiter;
import com.caston.challenge.server.RateLimitedRequestHandler;
import com.caston.challenge.server.RegisteringRequestHandler;
import com.caston.challenge.server.RequestHandler;
import com.caston.challenge.server.RequestRegistry;
import com.caston.challenge.server.UserBasedRateLimiter;
import com.caston.challenge.server.UserExtractor;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.time.Clock;
import java.time.Duration;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Basic HTTP handler for demoing the project which returns a very basic static
 * message to all non-ratelimited requests and status 429 for requests which
 * are rate limited.
 *
 * Depends on Oracle JRE for HTTP library.
 */
public class DemoHttpHandler implements HttpHandler {
  private static final Duration rateLimitWindow = Duration.ofSeconds(30);
  private static final Duration requestRegistryBuffer = Duration.ofSeconds(30);
  private static final int requestCountLimit = 10;
  private static final int SERVER_THREAD_COUNT = 8;

  private final Clock clock = Clock.systemUTC();
  private final UserExtractor<String, HttpExchange> userExtractor =
      new InetSocketHostExtractor();
  private final RequestRegistry<String, HttpExchange> requestRegistry =
      new RequestRegistry<>(clock, rateLimitWindow.plus(requestRegistryBuffer),
          userExtractor);
  private final RequestHandler<HttpExchange> handler =
      new RegisteringRequestHandler<String, HttpExchange>(requestRegistry,
          new RateLimitedRequestHandler<HttpExchange>(
              new UserBasedRateLimiter(userExtractor,
                  requestRegistry,
                  new RateLimitedResponseWriter(),
                  clock,
                  rateLimitWindow,
                  requestCountLimit),
          new StaticHttpRequestHandler()));

  /**
   * Creates an DemoHttpHandler instance on the specified path and port and
   * starts the server immediately.
   *
   * This call blocks the calling thread.
   */
  public static void blockingCreateAndStart(String path, int port)
      throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(port),
        /* backlog, use default */0);
    server.createContext(path, new DemoHttpHandler());
    server.setExecutor(Executors.newFixedThreadPool(SERVER_THREAD_COUNT));
    server.start();
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    handler.handle(exchange);
  }

  /**
   * Handler for non-rate limited requests.
   */
  private class StaticHttpRequestHandler
      implements RequestHandler<HttpExchange> {
    /**
     * Content sent in body of HTTP requests.
     *
     * Doesn't need to be anything fancy, just enough to verify things are
     * working as expected.
     */
    private static final String DEMO_RESPONSE = "Welcome to the demo page!";
    private static final int HTTP_STATUS_OK = 200;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
      exchange.sendResponseHeaders(HTTP_STATUS_OK, DEMO_RESPONSE.length());
      OutputStream outStream = exchange.getResponseBody();
      outStream.write(DEMO_RESPONSE.getBytes());
      outStream.close();
    }
  }

  /**
   * Handles output for exchanges which have been rate limited.
   */
  private class RateLimitedResponseWriter
      implements RequestHandler<HttpExchange> {
    private static final int HTTP_STATUS_TOO_MANY_REQ = 429;
    @Override
    public void handle(HttpExchange exchange) throws IOException {
      exchange.sendResponseHeaders(HTTP_STATUS_TOO_MANY_REQ, 0);
      exchange.close();
    }
  }
}
