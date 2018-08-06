package com.caston.challenge.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;

public class ServerTest {
  @Test
  public void testRateLimitedRequest() throws IOException {
    RateLimiter<String> mockLimiter = mock(RateLimiter.class);
    when(mockLimiter.rateLimitIfNecessary(anyString())).thenReturn(true);
    RequestHandler<String> mockHandler = mock(RequestHandler.class);
    Server<String> server = new Server<>(mockLimiter, mockHandler);

    server.handleRequest("Hello there!");

    verify(mockLimiter, times(1)).rateLimitIfNecessary("Hello there!");
    verify(mockHandler, never()).handle(anyString());
  }

  @Test
  public void testSuccessfulRequest() throws IOException {
    RateLimiter<String> mockLimiter = mock(RateLimiter.class);
    when(mockLimiter.rateLimitIfNecessary(anyString())).thenReturn(false);
    RequestHandler<String> mockHandler = mock(RequestHandler.class);
    Server<String> server = new Server<>(mockLimiter, mockHandler);

    server.handleRequest("Hello there!");

    verify(mockLimiter, times(1)).rateLimitIfNecessary("Hello there!");
    verify(mockHandler, times(1)).handle("Hello there!");
  }
}
