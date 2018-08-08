package com.caston.challenge.server;

import java.io.IOException;

import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Preconditions;

/**
 * A request handler which registers requests in a request registry before
 * delegating requests to an underlying request handler.
 */
@ThreadSafe
public class RegisteringRequestHandler<U, T> implements RequestHandler<T> {
  private final RequestRegistry<U, T> requestRegistry;
  private final RequestHandler<T> delegateHandler;

  /**
   * @param requestRegistry registry for all incoming requests to be registered
   *     in. Will be registered before delegating to underlying request handler.
   * @param delegateHandler handler for handling exchanges after they have been
   *     registered.
   */
  public RegisteringRequestHandler(RequestRegistry<U, T> requestRegistry,
      RequestHandler<T> delegateHandler) {
    this.requestRegistry = Preconditions.checkNotNull(requestRegistry);
    this.delegateHandler = Preconditions.checkNotNull(delegateHandler);
  }

  @Override
  public void handle(T exchange) throws IOException {
    requestRegistry.registerRequest(exchange);
    delegateHandler.handle(exchange);
  }
}

