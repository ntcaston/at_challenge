package com.caston.challenge.server;

import com.sun.net.httpserver.HttpExchange;

import javax.annotation.concurrent.ThreadSafe;

/**
 * UserExtractor which distinguishes users based on their host name.
 */
@ThreadSafe
public class InetSocketHostExtractor
    implements UserExtractor<String, HttpExchange> {
  @Override
  public String extractUser(HttpExchange exchange) {
    return exchange.getRemoteAddress().getHostName();
  }
}
