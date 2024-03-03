package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.client.bot.BotClient;
import edu.java.dto.client.LinkUpdate;
import java.net.URI;
import java.util.Collections;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

public class BotClientTest {
    private static final String API_LINK = "/updates";

    @Test
    @DisplayName("Bot client request test")
    @SneakyThrows
    public void sendUpdate_shouldSendRequestToClientServer() {
        WireMockServer server = new WireMockServer(8080);
        server.stubFor(post(urlPathMatching(API_LINK))
            .willReturn(aResponse()
                .withStatus(200)));
        server.start();
        LinkUpdate linkUpdate = new LinkUpdate(
            1L,
            new URI("https://google.com"),
            "test",
            Collections.emptyList()
        );
        BotClient botClient = new BotClient(makeClient());
        botClient.sendUpdate(linkUpdate);
        WireMock.verify(1, WireMock.postRequestedFor(WireMock.urlEqualTo(API_LINK)));
    }

    private WebClient makeClient() {
        return WebClient
            .builder()
            .baseUrl("http://localhost:8080")
            .build();
    }
}
