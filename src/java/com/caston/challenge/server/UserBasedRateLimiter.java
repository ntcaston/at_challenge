package com.caston.challenge.server;

import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Preconditions;

/**
 * Rate limiter implementation which limits requests on a per-user basis.
 */
@ThreadSafe
public class UserBasedRateLimiter<U, T> implements RateLimiter<T> {
  private final UserExtractor<U, T> userExtractor;
  private final ReadOnlyRequestRegistry<U> requestRegistry;
  private final RequestHandler<T> rateLimitedRequestHandler;
  private final Clock clock;
  private final Duration rateLimitWindow;
  private final int rateLimitRequestCount;

  /**
   * @param userExtractor defines how a user is to be determined for rate-
   *     limiting purposes.
   * @param requestRegistry used for measuring how many requests have been made
   *     by a particular user. Is assumed to be compatible with
   *     {@code userExtractor}. Requests should be registered before the rate
   *     limiter is invoked.
   * @param rateLimitedRequestHandler handler invoked on exchanges which are
   *     rate limited. Should perform minimal processing.
   * @param clock used for measuring when requests occur. Must be consistent
   *     with {@code requestRegistry}
   * @param rateLimitWindow the time window for which users are limited
   *     to {@code rateLimitRequestCount}.
   * @param rateLimitRequestCount the maximum number of requests that are
   *     allowed per user every {@code rateLimitWindow}
   */
  public UserBasedRateLimiter(UserExtractor<U, T> userExtractor,
      ReadOnlyRequestRegistry<U> requestRegistry,
      RequestHandler<T> rateLimitedRequestHandler,
      Clock clock,
      Duration rateLimitWindow,
      int rateLimitRequestCount) {
    this.userExtractor = Preconditions.checkNotNull(userExtractor);
    this.requestRegistry = Preconditions.checkNotNull(requestRegistry);
    this.rateLimitedRequestHandler =
        Preconditions.checkNotNull(rateLimitedRequestHandler);
    this.clock = Preconditions.checkNotNull(clock);
    this.rateLimitWindow=
        Preconditions.checkNotNull(rateLimitWindow);
    this.rateLimitRequestCount = rateLimitRequestCount;
  }

  @Override
  public boolean rateLimitIfNecessary(T exchange) throws IOException {
    Instant now = clock.instant();
    U user = userExtractor.extractUser(exchange);
    if (requestRegistry.getRequestCountForUser(
            user, now.minus(rateLimitWindow), now) > rateLimitRequestCount) {
      rateLimitedRequestHandler.handle(exchange);
      return true;
    }
    return false;
  }
}

