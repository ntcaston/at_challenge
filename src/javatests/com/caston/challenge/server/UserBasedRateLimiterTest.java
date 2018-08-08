package com.caston.challenge.server;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import org.junit.Test;

public class UserBasedRateLimiterTest {
  @Test
  public void testRateLimitedRequest() throws IOException {
    UserExtractor<String, Integer> mockUserExtractor =
        mock(UserExtractor.class);
    ReadOnlyRequestRegistry<String> mockRegistry =
        mock(ReadOnlyRequestRegistry.class);
    RequestHandler<Integer> mockHandler = mock(RequestHandler.class);
    FakeClock fakeClock = new FakeClock(Instant.ofEpochSecond(100));
    UserBasedRateLimiter<String, Integer> rateLimiter =
        new UserBasedRateLimiter<>(mockUserExtractor, mockRegistry, mockHandler,
            fakeClock, Duration.ofSeconds(30), /* request count */ 2);

    when(mockUserExtractor.extractUser(1)).thenReturn("1");
    when(mockRegistry.getRequestCountForUser("1", Instant.ofEpochSecond(70),
        Instant.ofEpochSecond(100))).thenReturn(1);
    assertFalse(rateLimiter.rateLimitIfNecessary(1));

    fakeClock.incrementBy(Duration.ofSeconds(10));
    when(mockRegistry.getRequestCountForUser("1", Instant.ofEpochSecond(80),
        Instant.ofEpochSecond(110))).thenReturn(2);
    assertFalse(rateLimiter.rateLimitIfNecessary(1));

    verify(mockHandler, never()).handle(any(Integer.class));
    fakeClock.incrementBy(Duration.ofSeconds(10));
    when(mockRegistry.getRequestCountForUser("1", Instant.ofEpochSecond(90),
        Instant.ofEpochSecond(120))).thenReturn(3);
    assertTrue(rateLimiter.rateLimitIfNecessary(1));
    verify(mockHandler, times(1)).handle(1);
  }
}

