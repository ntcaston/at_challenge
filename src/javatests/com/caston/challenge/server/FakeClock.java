package com.caston.challenge.server;

import java.time.Duration;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import com.google.common.base.Preconditions;

/**
 * Clock for testing purposes which allowers users to manipulate the time
 * manually.
 */
public final class FakeClock extends Clock {
  private final ZoneId zoneId;
  private Instant instant;

  public FakeClock(Instant startTime) {
    this(ZoneId.systemDefault(), startTime);
  }

  public FakeClock(ZoneId zoneId, Instant startTime) {
    this.zoneId = Preconditions.checkNotNull(zoneId);
    this.instant = Preconditions.checkNotNull(startTime);
  }

  public void incrementBy(Duration d) {
    instant = instant.plus(d);
  }

  @Override
  public ZoneId getZone() {
    return zoneId;
  }

  @Override
  public Instant instant() {
    return instant;
  }

  @Override
  public Clock withZone(ZoneId zone) {
    throw new UnsupportedOperationException();
  }
}
