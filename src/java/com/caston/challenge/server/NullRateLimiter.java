package com.caston.challenge.server;

/**
 * A rate limiter which never denies a request.
 */
public final class NullRateLimiter<T> implements RateLimiter<T> {
  @Override
  public boolean rateLimitIfNecessary(T exchange) {
    return false;
  }
}
