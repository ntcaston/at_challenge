package com.caston.challenge.server;

import java.time.Instant;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public interface ReadOnlyRequestRegistry<U> {
  int getRequestCountForUser(U user, Instant requestWindowStart,
      Instant requestWindowEnd);
}
