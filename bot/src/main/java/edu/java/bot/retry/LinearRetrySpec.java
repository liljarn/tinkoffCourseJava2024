package edu.java.bot.retry;

import edu.java.bot.exception.ScrapperServiceUnavailableException;
import java.time.Duration;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

@RequiredArgsConstructor
public class LinearRetrySpec extends Retry {
    private final Duration minBackoff;
    private final Duration maxBackoff = Duration.ofMillis(Long.MAX_VALUE);
    private final long maxAttempts;
    private final long step;
    private final Predicate<Throwable> errorFilter;

    public Flux<Long> generateCompanion(Flux<Retry.RetrySignal> t) {
        return Flux.deferContextual((cv) -> t.contextWrite(cv).concatMap((retryWhenState) -> {
            RetrySignal copy = retryWhenState.copy();
            Throwable currentFailure = copy.failure();
            long iteration = copy.totalRetries();
            if (currentFailure == null) {
                return Mono.error(new IllegalStateException("Retry.RetrySignal#failure() not expected to be null"));
            } else if (!this.errorFilter.test(currentFailure)) {
                return Mono.error(currentFailure);
            } else if (iteration >= this.maxAttempts) {
                return Mono.error(new ScrapperServiceUnavailableException());
            } else {
                Duration nextBackoff;
                try {
                    nextBackoff = this.minBackoff.multipliedBy(iteration * step);
                    if (nextBackoff.compareTo(this.maxBackoff) > 0) {
                        nextBackoff = this.maxBackoff;
                    }
                } catch (ArithmeticException e) {
                    nextBackoff = this.maxBackoff;
                }
                return Mono.delay(nextBackoff, Schedulers.parallel()).contextWrite(cv);
            }
        }).onErrorStop());
    }
}
