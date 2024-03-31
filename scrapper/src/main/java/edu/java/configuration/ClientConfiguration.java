package edu.java.configuration;

import edu.java.client.github.GitHubInfoProvider;
import edu.java.client.github.events.EventProvider;
import edu.java.client.stackoverflow.StackOverflowInfoProvider;
import edu.java.retry.RetryFactory;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {
    @Value("${client.bot.base-url}")
    private String botBaseUrl;
    @Value("${client.github.base-url}")
    private String githubBaseUrl;
    @Value("${client.stackoverflow.base-url}")
    private String stackoverflowBaseUrl;

    @SuppressWarnings("checkstyle:MultipleStringLiterals") @Bean
    public WebClient webClient(RetryConfiguration retryConfiguration) {
        return (botBaseUrl == null || botBaseUrl.isEmpty())
            ? WebClient.builder().baseUrl("http://localhost:8090")
            .filter(RetryFactory.buildFilter(RetryFactory.createRetry(retryConfiguration, "bot"))).build()
            : WebClient.builder().baseUrl(botBaseUrl)
            .filter(RetryFactory.buildFilter(RetryFactory.createRetry(retryConfiguration, "bot"))).build();
    }

    @Bean
    public GitHubInfoProvider gitHubInfoProvider(
        List<EventProvider> eventProviderList,
        RetryConfiguration retryConfiguration
    ) {
        return (githubBaseUrl == null || githubBaseUrl.isEmpty())
            ? new GitHubInfoProvider(eventProviderList, retryConfiguration)
            : new GitHubInfoProvider(githubBaseUrl, eventProviderList, retryConfiguration);
    }

    @Bean
    public StackOverflowInfoProvider stackOverflowInfoProvider(RetryConfiguration retryConfiguration) {
        return (stackoverflowBaseUrl == null || stackoverflowBaseUrl.isEmpty())
            ? new StackOverflowInfoProvider(retryConfiguration)
            : new StackOverflowInfoProvider(stackoverflowBaseUrl, retryConfiguration);
    }
}
