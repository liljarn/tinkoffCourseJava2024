package edu.java.client;

import org.springframework.web.reactive.function.client.WebClient;

public abstract class WebClientInfoProvider implements ClientInfoProvider {
    protected final WebClient webClient;

    public WebClientInfoProvider(String apiUrl) {
        webClient = WebClient.create(apiUrl);
    }
}
