package com.caston.challenge.server;

import java.io.IOException;

import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Preconditions;

/**
 * RequestHandler which restricts queries to an underlying RequestHandler as
 * dictated by a rate limiter.
 */
@ThreadSafe
public final class RateLimitedRequestHandler<T> implements RequestHandler<T> {
  private final RateLimiter<T> rateLimiter;
  private final RequestHandler<T> baseHandler;

  /**
   * @param rateLimiter the rate limiter to be applied to all incoming requests.
   * @param baseHandler responsible for handling requests which are not rate
   *     rate limited. Should not perform its own rate limiting.
   */
  public RateLimitedRequestHandler(RateLimiter<T> rateLimiter,
        RequestHandler<T> baseHandler) {
    this.rateLimiter = Preconditions.checkNotNull(rateLimiter);
    this.baseHandler = Preconditions.checkNotNull(baseHandler);
  }

  /**
   * Handles the exchange by either denying it if the rate limiter dictates, or
   * by handling it with the underlying base handler object.
   */
  @Override
  public void handle(T exchange) throws IOException {
    if (rateLimiter.rateLimitIfNecessary(exchange)) {
      return;
    }
    baseHandler.handle(exchange);
  }
}
