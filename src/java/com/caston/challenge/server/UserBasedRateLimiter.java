package com.caston.challenge.server;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;

import javax.annotation.concurrent.ThreadSafe;

/**
 */
@ThreadSafe
public class UserBasedRateLimiter<U, T> implements RateLimiter<T> {
  private final UserExtractor<U, T> userExtractor;
  private final ReadOnlyRequestRegistry<U> requestRegistry;
  private final RequestHandler<T> rateLimitedRequestHandler;
  private final Clock clock;
  private final long rateLimitWindowSeconds;
  private final int rateLimitRequestCount;

  public UserBasedRateLimiter(UserExtractor<U, T> userExtractor,
      ReadOnlyRequestRegistry<U> requestRegistry,
      RequestHandler<T> rateLimitedRequestHandler,
      Clock clock,
      long rateLimitWindowSeconds,
      int rateLimitRequestCount) {
    // TODO(ntcaston): Preconditions.checkNotNull
    this.userExtractor = userExtractor;
    this.requestRegistry = requestRegistry;
    this.rateLimitedRequestHandler = rateLimitedRequestHandler;
    this.clock = clock;
    this.rateLimitWindowSeconds = rateLimitWindowSeconds;
    this.rateLimitRequestCount = rateLimitRequestCount;
  }

  @Override
  public boolean rateLimitIfNecessary(T exchange) throws IOException {
    Instant now = clock.instant();
    U user = userExtractor.extractUser(exchange);
    if (requestRegistry.getRequestCountForUser(user,
            now.minusSeconds(rateLimitWindowSeconds), now) >
        rateLimitRequestCount) {
      rateLimitedRequestHandler.handle(exchange);
      return true;
    }
    return false;
  }
}

