package edu.java.bot.configuration;

import edu.java.bot.retry.RetryFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {
    @Value("${client.bot.base-url}")
    private String botUrl;

    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @Bean
    public WebClient webClient(RetryConfiguration retryConfiguration) {
        return (botUrl == null || botUrl.isEmpty())
            ? WebClient.builder().baseUrl("http://localhost:8080")
            .filter(RetryFactory.buildFilter(RetryFactory.createRetry(retryConfiguration, "scrapper"))).build()
            : WebClient.builder().baseUrl(botUrl)
            .filter(RetryFactory.buildFilter(RetryFactory.createRetry(retryConfiguration, "scrapper"))).build();
    }
}
