package com.caston.challenge.server;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Extracts a user from an exchange.
 */
@ThreadSafe
public interface UserExtractor<U, T> {
  /**
   * Returns the user assosciated with an exchange. Implementations must not
   * mutate the exchange and must return the same user if called multiple times
   * with the same exchange.
   */
  U extractUser(T exchange);
}
