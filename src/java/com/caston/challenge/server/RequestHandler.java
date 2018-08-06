package com.caston.challenge.server;

import java.io.IOException;

/**
 * Responsible for servicing incoming requests and updating the response as
 * necessary.
 */
public interface RequestHandler<T> {
  /**
   * Handles and single request-response exchange.
   *
   * This method must be thread-safe.
   */
  // TODO(ntcaston): ThreadSafe annotation
  void handle(T exchange) throws IOException;
}
