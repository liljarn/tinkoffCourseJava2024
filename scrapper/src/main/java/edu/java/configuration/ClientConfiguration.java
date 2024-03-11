package edu.java.configuration;

import edu.java.client.github.GitHubInfoProvider;
import edu.java.client.stackoverflow.StackOverflowInfoProvider;
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


    @Bean
    public WebClient webClient() {
        return (botBaseUrl.isEmpty()) ? WebClient.builder().baseUrl("http://localhost:8090").build()
            : WebClient.builder().baseUrl(botBaseUrl).build();
    }

    @Bean
    public GitHubInfoProvider gitHubInfoProvider() {
        if (githubBaseUrl == null || githubBaseUrl.isEmpty()) {
            return new GitHubInfoProvider();
        }
        return new GitHubInfoProvider(githubBaseUrl);
    }

    @Bean
    public StackOverflowInfoProvider stackOverflowInfoProvider() {
        if (stackoverflowBaseUrl == null || stackoverflowBaseUrl.isEmpty()) {
            return new StackOverflowInfoProvider();
        }
        return new StackOverflowInfoProvider(stackoverflowBaseUrl);
    }
}
