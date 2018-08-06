package com.caston.challenge.server;

import java.io.IOException;

/**
 * Main server for handling incoming requests.
 */
public final class Server<T> {
  private final RateLimiter<T> rateLimiter;
  private final RequestHandler<T> requestHandler;

  /**
   * @param rateLimiter the rate limiter to be applied to all incoming requests.
   * @param requestHandler responsible for handling requests which are not rate
   *     rate limited. Should not perform its own rate limiting.
   */
  public Server(RateLimiter<T> rateLimiter, RequestHandler<T> requestHandler) {
    // TODO(ntcaston): Preconditions.checkNotNull
    this.rateLimiter = rateLimiter;
    this.requestHandler = requestHandler;
  }

  /**
   * Handles the exchange by either denying it if the rate limiter dictates, or
   * by handling it with this classes RequestHandler object.
   *
   * Performs logic synchronously; it is the callers responsibility to handle
   * assigning exhanges to threads.
   *
   * This method is thread-safe.
   */
  // TODO(ntcaston): ThreadSafe annotation
  public void handleRequest(T exchange) throws IOException {
    if (rateLimiter.rateLimitIfNecessary(exchange)) {
      return;
    }
    requestHandler.handle(exchange);
  }
}
