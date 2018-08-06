package com.caston.challenge.server;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public interface UserExtractor<U, T> {
  U extractUser(T exchange);
}
