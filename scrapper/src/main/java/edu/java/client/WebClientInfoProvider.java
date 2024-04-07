package edu.java.client;

import edu.java.configuration.RetryConfiguration;
import edu.java.retry.RetryFactory;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class WebClientInfoProvider implements ClientInfoProvider {
    protected final WebClient webClient;

    public WebClientInfoProvider(String apiUrl, RetryConfiguration config, String client) {
        webClient = WebClient.builder().baseUrl(apiUrl)
            .filter(RetryFactory.buildFilter(RetryFactory.createRetry(config, client))).build();
    }
}
