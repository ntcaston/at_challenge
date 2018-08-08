package com.caston.challenge.server;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Preconditions;

/**
 * Stores and provides lookup of requests. In order to keep memory usage bounded
 * requests that are older than a specified age will be removed from the
 * registry.
 *
 * In order to avoid completely locking, this class may not give completely
 * accurate results if requests are registered for a user while reading,
 * however errors from the true value should be minimal except in the most
 * extreme cases.
 */
@ThreadSafe
public final class RequestRegistry<U, T> implements ReadOnlyRequestRegistry<U> {
  // The number of requests between garbage collection events. Ideally
  // collection would be done periodically on a background thread to reduce
  // impact on user query latency.
  private static final int REQUESTS_BETWEEN_GC = 100;

  private final Clock clock;
  private final Duration ageLimit;
  private final UserExtractor<U, T> userExtractor;

  // NOTE(ntcaston): A tree structure or a binary-search-friendly list would
  // probably be better suited to this task.
  private final ConcurrentHashMap<U, ConcurrentLinkedDeque<Instant>> requests;
  private final AtomicInteger requestsSinceLastGarbageCollect;

  /**
   * @param clock the clock that will be used when determining both 1) when the
   *     requests occur, and 2) the age of requests during eviction. It is
   *     assumed that the clock is non-decreasing in time - so localized
   *     timezones which can move backwards (e.g. day light savings) should not
   *     be used.
   * @param ageLimit how old requests may be to make them available to be
   *     unregistered.
   */
  public RequestRegistry(Clock clock, Duration ageLimit,
      UserExtractor<U, T> userExtractor) {
    this.clock = Preconditions.checkNotNull(clock);
    this.ageLimit = Preconditions.checkNotNull(ageLimit);
    this.userExtractor = Preconditions.checkNotNull(userExtractor);
    requests = new ConcurrentHashMap<>();
    requestsSinceLastGarbageCollect = new AtomicInteger();
  }

  /**
   * Stores a request in the registry.
   */
  public void registerRequest(T exchange) {
    U user = userExtractor.extractUser(exchange);
    Instant now = clock.instant();
    ConcurrentLinkedDeque<Instant> tmpList =
        new ConcurrentLinkedDeque<Instant>();
    ConcurrentLinkedDeque<Instant> listToUpdate =
        requests.putIfAbsent(user, tmpList);
    if (listToUpdate == null) {
      listToUpdate = tmpList;
    }
    listToUpdate.add(now);
    if (requestsSinceLastGarbageCollect.incrementAndGet() >
        REQUESTS_BETWEEN_GC) {
      requestsSinceLastGarbageCollect.addAndGet(-REQUESTS_BETWEEN_GC);
      garbageCollectOldEntries();
    }
  }

  @Override
  public int getRequestCountForUser(U user, Instant requestWindowStart,
      Instant requestWindowEnd) {
    if (!requests.containsKey(user)) {
      return 0;
    }

    // NOTE(ntcaston): Could theoretically just find the position of the first
    // and last elements within the time range.
    int count = 0;
    // ConcurrentLinkedDeque iterators are weakly consistent and will not throw
    // ConcurrentModificationExcpetion during usage. The result may not be
    // be perfectly accurate if modified while iterating.
    for (Instant requestTime : requests.get(user)) {
      if (requestTime.equals(requestWindowStart) ||
              (requestTime.isAfter(requestWindowStart) &&
              requestTime.isBefore(requestWindowEnd))) {
        count++;
      }
    }
    return count;
  }

  /**
   * Deletes entries if they are older than {@code ageLimit}. May also remove
   * the user-key if the list of requests for that user becomes empty, this may
   * introduce an incorrect count under concurrent modification.
   *
   * It is important to garbage collect to prevent the size of the underlying
   * data structures from growing to an unbounded size for a long-lived service.
   */
  private void garbageCollectOldEntries() {
    Instant oldestAllowed = clock.instant().minus(ageLimit);

    // ConcurrentHashMap is weakly consistent during iteration and will not
    // throw an exception due to concurrent modification.
    Iterator<Map.Entry<U, ConcurrentLinkedDeque<Instant>>> mapIt =
        requests.entrySet().iterator();
    while (mapIt.hasNext()) {
      Map.Entry<U, ConcurrentLinkedDeque<Instant>> entry = mapIt.next();
      Iterator<Instant> listIt = entry.getValue().iterator();
      while (listIt.hasNext()) {
        if (listIt.next().isBefore(oldestAllowed)) {
          listIt.remove();
        }
      }
      if (entry.getValue().isEmpty()) {
        mapIt.remove();
      }
    }
  }
}
