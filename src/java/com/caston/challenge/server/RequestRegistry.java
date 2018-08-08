package com.caston.challenge.server;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Preconditions;

/**
 * Stores and provides lookup of requests. In order to keep memory usage bounded
 * requests that are older than a specified age will be removed from the
 * registry.
 */
@ThreadSafe
public class RequestRegistry<U, T> implements ReadOnlyRequestRegistry<U> {
  private final Clock clock;
  private final Duration ageLimit;
  private final UserExtractor<U, T> userExtractor;

  /**
   * @param clock the clock that will be used when determining both 1) when the
   *     requests occur, and 2) the age of requests during eviction. It is
   *     assumed that the clock is non-decreasing in time - so localized
   *     timezones which can move backwards (e.g. day light savings) should not
   *     be used.
   * @param ageLimit how old requests may be to make them available for
   *     unregistered.
   */
  public RequestRegistry(Clock clock, Duration ageLimit,
      UserExtractor<U, T> userExtractor) {
    this.clock = Preconditions.checkNotNull(clock);
    this.ageLimit = Preconditions.checkNotNull(ageLimit);
    this.userExtractor = Preconditions.checkNotNull(userExtractor);
  }

  /**
   * Stores a request in the registry.
   */
  public void registerRequest(T exchange) {
    // TODO(ntcaston): Implement.
    U user = userExtractor.extractUser(exchange);
    System.out.println("RequestRegistry received request from: " + user);
  }

  @Override
  public int getRequestCountForUser(U user, Instant requestWindowStart,
      Instant requestWindowEnd) {
    // TODO(ntcaston): Implement.
    return 0;
  }
}
