package com.caston.challenge.server;

import java.time.Instant;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class RequestRegistry<U, T> implements ReadOnlyRequestRegistry<U> {
  private final UserExtractor<U, T> userExtractor;

  public RequestRegistry(UserExtractor<U, T> userExtractor) {
    // TODO(ntcaston): Preconditions
    this.userExtractor = userExtractor;
  }

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
