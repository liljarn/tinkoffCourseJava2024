package edu.java.bot.retry;

import edu.java.bot.configuration.RetryConfiguration;
import edu.java.bot.exception.ScrapperServiceUnavailableException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

@UtilityClass
@Log4j2
public class RetryFactory {
    private static final Map<String, Function<RetryConfiguration.RetryInfo, Retry>> RETRY_MAP = new HashMap<>();

    static {
        RETRY_MAP.put(
            "fixed",
            retry -> RetryBackoffSpec.fixedDelay(retry.maxAttempts(), retry.delay())
                .filter(buildErrorList(retry.codes())).onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    throw new ScrapperServiceUnavailableException();
                })
        );
        RETRY_MAP.put(
            "linear",
            retry -> new LinearRetrySpec(
                retry.delay(),
                retry.maxAttempts(),
                retry.step(),
                buildErrorList(retry.codes())
            )
        );
        RETRY_MAP.put(
            "exponential",
            retry -> RetryBackoffSpec.backoff(retry.maxAttempts(), retry.delay()).jitter(0.0)
                .filter(buildErrorList(retry.codes())).onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    throw new ScrapperServiceUnavailableException();
                })
        );
    }

    public static ExchangeFilterFunction buildFilter(Retry retry) {
        return (request, next) -> next.exchange(request)
            .flatMap(clientResponse -> Mono.just(clientResponse)
                .filter(response -> clientResponse.statusCode().isError())
                .flatMap(response -> clientResponse.createException())
                .flatMap(Mono::error)
                .thenReturn(clientResponse))
            .retryWhen(retry);
    }

    public static Retry createRetry(RetryConfiguration config, String client) {
        return config
            .retries()
            .stream()
            .filter(retry -> retry.client().equals(client))
            .findFirst()
            .map(retry -> RETRY_MAP.get(retry.type()).apply(retry))
            .orElseThrow(IllegalStateException::new);
    }

    private static Predicate<Throwable> buildErrorList(List<Integer> codes) {
        return throwable -> {
            if (throwable instanceof WebClientResponseException e) {
                return codes.contains(e.getStatusCode().value());
            }
            return throwable instanceof WebClientRequestException;
        };
    }
}

