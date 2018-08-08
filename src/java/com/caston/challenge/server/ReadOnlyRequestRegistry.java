package com.caston.challenge.server;

import java.time.Instant;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Read-only access to the request registry.
 */
@ThreadSafe
public interface ReadOnlyRequestRegistry<U> {
  /**
   * Returns the number of requests registered to a user within the given time
   * period.
   *
   * @param user the user to count requests for.
   * @param requestWindowStart the inclusive lower bound of the time window.
   * @param requestWindowEnd the exclusive upper bound of the time window.
   */
  int getRequestCountForUser(U user, Instant requestWindowStart,
      Instant requestWindowEnd);
}
