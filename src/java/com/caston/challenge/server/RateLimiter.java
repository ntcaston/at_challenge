package com.caston.challenge.server;

import java.io.IOException;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Responsible for determining if a request should be permitted and updating the
 * reponse for rate limited exchanges.
 */
@ThreadSafe
public interface RateLimiter<T> {
  /**
   * Determines if an exchange should be rate limited. If the exchange is to be
   * rate limited, this method will return true and perform any necessary
   * updates to the exchange to complete it. If the exchange should not be rate
   * limited, this method will return false and callers should handle this
   * exchange normally.
   *
   * This method must be thread-safe.
   *
   * @return true iff {@code exchange} is being rate limited and should not be
   *     handled.
   */
  boolean rateLimitIfNecessary(T exchange) throws IOException;
}
