package edu.java.client.bot;

import edu.java.dto.client.LinkUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class BotClient {
    private final WebClient webClient;

    public void sendUpdate(LinkUpdate linkUpdate) {
        webClient
            .post()
            .uri("/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(linkUpdate)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
}
