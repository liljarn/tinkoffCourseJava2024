package edu.java.configuration;

import edu.java.client.github.GitHubInfoProvider;
import edu.java.client.stackoverflow.StackOverflowInfoProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {
    @Bean
    public GitHubInfoProvider gitHubInfoProvider() {
        return new GitHubInfoProvider();
    }

    @Bean
    public StackOverflowInfoProvider stackOverflowInfoProvider() {
        return new StackOverflowInfoProvider();
    }

    @Bean
    public WebClient botWebClient() {
        return null;
    }
}
