package io.sentry;

import io.sentry.protocol.Sessions;
import io.sentry.util.Objects;
import java.io.Closeable;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import org.jetbrains.annotations.NotNull;

/** Periodically flushes aggregated server side {@link Sessions} to Sentry. */
final class SessionsFlusher implements Closeable {
  private static final int ONE_MINUTE = 60 * 1000;

  private final @NotNull SessionAggregates aggregates;
  private final @NotNull ISentryClient sentryClient;
  private final @NotNull Timer timer = new Timer();
  private final long delay;
  private final long period;

  public SessionsFlusher(
      final @NotNull SessionAggregates aggregates, final @NotNull ISentryClient sentryClient) {
    this(aggregates, sentryClient, ONE_MINUTE, ONE_MINUTE);
  }

  SessionsFlusher(
      final @NotNull SessionAggregates sessionAggregates,
      final @NotNull ISentryClient sentryClient,
      final long delay,
      final long period) {
    this.aggregates = Objects.requireNonNull(sessionAggregates, "sessionAggregates is required");
    this.sentryClient = Objects.requireNonNull(sentryClient, "sentryClient is required");
    this.delay = delay;
    this.period = period;
  }

  void start() {
    timer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            flush();
          }
        },
        delay,
        period);
  }

  void flush() {
    // atomic reference swap
    if (!aggregates.getAggregates().isEmpty()) {
      sentryClient.captureSessions(new Sessions(aggregates));
      aggregates.getAggregates().clear();
    }
  }

  @Override
  public void close() throws IOException {
    timer.cancel();
  }
}
