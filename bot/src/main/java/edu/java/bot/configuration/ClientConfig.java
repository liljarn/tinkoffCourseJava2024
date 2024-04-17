package edu.java.bot.configuration;

import edu.java.bot.dto.response.ApiErrorResponse;
import edu.java.bot.exception.ScrapperException;
import edu.java.bot.retry.RetryFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class ClientConfig {
    @Value("${client.bot.base-url}")
    private String botUrl;

    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @Bean
    public WebClient webClient(RetryConfiguration retryConfiguration) {
        return (botUrl == null || botUrl.isEmpty())
            ? WebClient.builder().baseUrl("http://localhost:8080")
            .filter(
                RetryFactory.buildFilter(RetryFactory.createRetry(retryConfiguration, "scrapper"))
                    .andThen(
                        ExchangeFilterFunction.ofResponseProcessor(ClientConfig::handleError)
                    )
            )
            .build()
            : WebClient.builder().baseUrl(botUrl)
            .filter(
                RetryFactory.buildFilter(RetryFactory.createRetry(retryConfiguration, "scrapper"))
                    .andThen(
                        ExchangeFilterFunction.ofResponseProcessor(ClientConfig::handleError)
                    )
            )
            .build();
    }

    private static Mono<ClientResponse> handleError(ClientResponse clientResponse) {
        if (clientResponse.statusCode().isError()) {
            return clientResponse.bodyToMono(ApiErrorResponse.class)
                .flatMap(apiErrorResponse -> Mono.error(new ScrapperException(
                    apiErrorResponse.description(),
                    HttpStatus.valueOf(Integer.parseInt(apiErrorResponse.code())),
                    apiErrorResponse.exceptionMessage()
                )));
        }
        return Mono.just(clientResponse);
    }
}
