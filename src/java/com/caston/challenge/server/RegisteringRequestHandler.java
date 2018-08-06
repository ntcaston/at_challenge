package com.caston.challenge.server;

import java.io.IOException;

import javax.annotation.concurrent.ThreadSafe;

/**
 * A request handler which registers requests in a request registry before
 * delegating requests to an underlying request handler.
 */
@ThreadSafe
public class RegisteringRequestHandler<U, T> implements RequestHandler<T> {
  private final RequestRegistry<U, T> requestRegistry;
  private final RequestHandler<T> delegateHandler;

  public RegisteringRequestHandler(RequestRegistry<U, T> requestRegistry,
      RequestHandler<T> delegateHandler) {
    // TODO(ntcaston): Preconditions.
    this.requestRegistry = requestRegistry;
    this.delegateHandler = delegateHandler;
  }

  @Override
  public void handle(T exchange) throws IOException {
    requestRegistry.registerRequest(exchange);
    delegateHandler.handle(exchange);
  }
}

