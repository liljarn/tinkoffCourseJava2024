package edu.java.bot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {
    @Value("${client.bot.base-url}")
    private String botUrl;

    @Bean
    public WebClient webClient() {
        return (botUrl == null || botUrl.isEmpty()) ? WebClient.builder().baseUrl("http://localhost:8080").build()
            : WebClient.builder().baseUrl(botUrl).build();
    }
}
