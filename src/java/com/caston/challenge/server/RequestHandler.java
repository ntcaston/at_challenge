package com.caston.challenge.server;

import java.io.IOException;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Responsible for servicing incoming requests and updating the response as
 * necessary.
 */
@ThreadSafe
public interface RequestHandler<T> {
  /**
   * Handles and single request-response exchange.
   *
   * Blocks until completion. It is the caller's responsibility to handle
   * delegating calls to threads.
   *
   * This method must be thread-safe.
   */
  void handle(T exchange) throws IOException;
}
