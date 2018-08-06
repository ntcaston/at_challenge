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
   * This method must be thread-safe.
   */
  void handle(T exchange) throws IOException;
}
