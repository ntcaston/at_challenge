package com.caston.challenge.server;

import com.sun.net.httpserver.HttpExchange;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class InetSocketHostExtractor
    implements UserExtractor<String, HttpExchange> {
  @Override
  public String extractUser(HttpExchange exchange) {
    return exchange.getRemoteAddress().getHostName();
  }
}
