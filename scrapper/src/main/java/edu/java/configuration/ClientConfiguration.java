package edu.java.configuration;

import edu.java.client.github.GitHubInfoProvider;
import edu.java.client.stackoverflow.StackOverflowInfoProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {
    @Value("client.bot.base-url")
    private String botUrl;
    @Value("client.github.base-url")
    private String githubUrl;
    @Value("client.stackoverflow.base-url")
    private String stackoverflowUrl;

    @Bean
    public WebClient webClient() {
        return (botUrl.isEmpty()) ? WebClient.builder().baseUrl("http://localhost:8090").build()
            : WebClient.builder().baseUrl(botUrl).build();
    }

    @Bean
    public GitHubInfoProvider gitHubInfoProvider() {
        if (githubUrl.isEmpty()) {
            return new GitHubInfoProvider();
        }
        return new GitHubInfoProvider(githubUrl);

    @Bean
    public StackOverflowInfoProvider stackOverflowInfoProvider() {
        if (stackoverflowUrl.isEmpty()) {
            return new StackOverflowInfoProvider();
        }
        return new StackOverflowInfoProvider(stackoverflowUrl);
    }
}
