package com.caston.challenge.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import org.junit.Test;

public class RequestRegistryTest {
  @Test
  public void testSingleRequest() {
    FakeClock fakeClock = new FakeClock(Instant.ofEpochSecond(100));
    UserExtractor<String, String> mockUserExtractor =
        mock(UserExtractor.class);
    RequestRegistry<String, String> registry =
        new RequestRegistry<>(fakeClock,
            Duration.ofSeconds(10000), mockUserExtractor);

    assertEquals(0, registry.getRequestCountForUser("abc",
        Instant.ofEpochSecond(0), Instant.ofEpochSecond(999)));

    when(mockUserExtractor.extractUser("exchange1")).thenReturn("abc");
    registry.registerRequest("exchange1");
    assertEquals(1, registry.getRequestCountForUser("abc",
        Instant.ofEpochSecond(0), Instant.ofEpochSecond(999)));
    assertEquals(1, registry.getRequestCountForUser("abc",
        Instant.ofEpochSecond(0), Instant.ofEpochSecond(101)));
    assertEquals(1, registry.getRequestCountForUser("abc",
        Instant.ofEpochSecond(100), Instant.ofEpochSecond(999)));

    assertEquals(0, registry.getRequestCountForUser("abc",
        Instant.ofEpochSecond(0), Instant.ofEpochSecond(100)));
    assertEquals(0, registry.getRequestCountForUser("abc",
        Instant.ofEpochSecond(101), Instant.ofEpochSecond(999)));
  }

  @Test
  public void testMultipleRequestsSingleUser() {
    FakeClock fakeClock = new FakeClock(Instant.ofEpochSecond(100));
    UserExtractor<String, String> mockUserExtractor =
        mock(UserExtractor.class);
    RequestRegistry<String, String> registry =
        new RequestRegistry<>(fakeClock,
            Duration.ofSeconds(10000), mockUserExtractor);

    assertEquals(0, registry.getRequestCountForUser("abc",
        Instant.ofEpochSecond(0), Instant.ofEpochSecond(999)));

    when(mockUserExtractor.extractUser("exchange1")).thenReturn("abc");
    registry.registerRequest("exchange1");
    fakeClock.incrementBy(Duration.ofSeconds(1));
    registry.registerRequest("exchange1");
    fakeClock.incrementBy(Duration.ofSeconds(1));
    registry.registerRequest("exchange1");
    fakeClock.incrementBy(Duration.ofSeconds(1));
    registry.registerRequest("exchange1");

    assertEquals(4, registry.getRequestCountForUser("abc",
        Instant.ofEpochSecond(0), Instant.ofEpochSecond(999)));
    assertEquals(3, registry.getRequestCountForUser("abc",
        Instant.ofEpochSecond(101), Instant.ofEpochSecond(999)));
    assertEquals(2, registry.getRequestCountForUser("abc",
        Instant.ofEpochSecond(101), Instant.ofEpochSecond(103)));
    assertEquals(1, registry.getRequestCountForUser("abc",
        Instant.ofEpochSecond(102), Instant.ofEpochSecond(103)));
    assertEquals(0, registry.getRequestCountForUser("abc",
        Instant.ofEpochSecond(104), Instant.ofEpochSecond(105)));
  }

  @Test
  public void testMultipleUsers() {
    FakeClock fakeClock = new FakeClock(Instant.ofEpochSecond(100));
    UserExtractor<String, String> mockUserExtractor =
        mock(UserExtractor.class);
    RequestRegistry<String, String> registry =
        new RequestRegistry<>(fakeClock,
            Duration.ofSeconds(10000), mockUserExtractor);

    assertEquals(0, registry.getRequestCountForUser("user1",
        Instant.ofEpochSecond(0), Instant.ofEpochSecond(999)));
    assertEquals(0, registry.getRequestCountForUser("user2",
        Instant.ofEpochSecond(0), Instant.ofEpochSecond(999)));
    assertEquals(0, registry.getRequestCountForUser("user3",
        Instant.ofEpochSecond(0), Instant.ofEpochSecond(999)));

    when(mockUserExtractor.extractUser("exchange1")).thenReturn("user1");
    when(mockUserExtractor.extractUser("exchange2")).thenReturn("user2");
    when(mockUserExtractor.extractUser("exchange3")).thenReturn("user3");

    registry.registerRequest("exchange1");
    fakeClock.incrementBy(Duration.ofSeconds(1));
    registry.registerRequest("exchange2");
    fakeClock.incrementBy(Duration.ofSeconds(1));
    registry.registerRequest("exchange1");
    fakeClock.incrementBy(Duration.ofSeconds(1));
    registry.registerRequest("exchange2");
    fakeClock.incrementBy(Duration.ofSeconds(1));
    registry.registerRequest("exchange3");

    // Over whole time range.
    assertEquals(2, registry.getRequestCountForUser("user1",
        Instant.ofEpochSecond(0), Instant.ofEpochSecond(999)));
    assertEquals(2, registry.getRequestCountForUser("user2",
        Instant.ofEpochSecond(0), Instant.ofEpochSecond(999)));
    assertEquals(1, registry.getRequestCountForUser("user3",
        Instant.ofEpochSecond(0), Instant.ofEpochSecond(999)));

    // Restricted time range.
    assertEquals(1, registry.getRequestCountForUser("user1",
        Instant.ofEpochSecond(101), Instant.ofEpochSecond(999)));
    assertEquals(2, registry.getRequestCountForUser("user2",
        Instant.ofEpochSecond(101), Instant.ofEpochSecond(999)));
    assertEquals(1, registry.getRequestCountForUser("user3",
        Instant.ofEpochSecond(101), Instant.ofEpochSecond(999)));
    assertEquals(1, registry.getRequestCountForUser("user1",
        Instant.ofEpochSecond(101), Instant.ofEpochSecond(103)));
    assertEquals(1, registry.getRequestCountForUser("user2",
        Instant.ofEpochSecond(101), Instant.ofEpochSecond(103)));
    assertEquals(0, registry.getRequestCountForUser("user3",
        Instant.ofEpochSecond(101), Instant.ofEpochSecond(103)));
    assertEquals(0, registry.getRequestCountForUser("user1",
        Instant.ofEpochSecond(103), Instant.ofEpochSecond(105)));
    assertEquals(0, registry.getRequestCountForUser("user2",
        Instant.ofEpochSecond(104), Instant.ofEpochSecond(105)));
  }
}
