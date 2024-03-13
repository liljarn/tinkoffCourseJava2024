package edu.java.bot.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.bot.dto.client.AddLinkRequest;
import edu.java.bot.dto.client.LinkResponse;
import edu.java.bot.dto.client.ListLinksResponse;
import edu.java.bot.dto.client.RemoveLinkRequest;
import java.net.URI;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.Assertions.assertThat;

public class ScrapperClientTest {
    private static final String CHAT_LINK = "/tg-chat";
    private static final String LINK = "/links";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private WireMockServer server;

    @Test
    @DisplayName("Scrapper client request to ChatController registerChat test")
    @SneakyThrows
    public void registerChat_shouldSendRequestToChatController() {
        //Arrange
        server = new WireMockServer(8080);
        server.stubFor(post(urlPathMatching(CHAT_LINK))
            .willReturn(aResponse()
                .withStatus(200)));
        server.start();
        ScrapperClient scrapperClient = new ScrapperClient(makeClient());
        //Act
        scrapperClient.registerChat(1L);
        //Assert
        WireMock.verify(1, WireMock.postRequestedFor(WireMock.urlEqualTo(CHAT_LINK)));
    }

    @Test
    @DisplayName("Scrapper client request to ChatController deleteChat test")
    @SneakyThrows
    public void delete_chat_shouldSendRequestToChatController() {
        //Arrange
        server = new WireMockServer(8080);
        server.stubFor(delete(urlPathMatching(CHAT_LINK))
            .willReturn(aResponse()
                .withStatus(200)));
        server.start();
        ScrapperClient scrapperClient = new ScrapperClient(makeClient());
        //Act
        scrapperClient.deleteChat(1L);
        //Assert
        WireMock.verify(1, WireMock.deleteRequestedFor(WireMock.urlEqualTo(CHAT_LINK)));
    }

    @Test
    @DisplayName("Scrapper client request to LinkController getLinks test")
    @SneakyThrows
    public void getLinks_shouldSendRequestToLinkController() {
        //Arrange
        server = new WireMockServer(8080);
        server.stubFor(get(urlPathMatching(LINK))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(objectMapper.writeValueAsString(new ListLinksResponse(
                    List.of(new LinkResponse(100L, URI.create("test.com"))),
                    1
                )))));
        server.start();
        ScrapperClient scrapperClient = new ScrapperClient(makeClient());
        ListLinksResponse actual = new ListLinksResponse(List.of(new LinkResponse(100L, URI.create("test.com"))), 1);
        //Act
        ListLinksResponse response = scrapperClient.getLinks(1L);
        //Assert
        assertThat(response).isEqualTo(actual);
    }

    @Test
    @DisplayName("Scrapper client request to LinkController addLink test")
    @SneakyThrows
    public void addLink_shouldSendRequestToLinkController() {
        //Arrange
        server = new WireMockServer(8080);
        server.stubFor(post(urlPathMatching(LINK))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(objectMapper.writeValueAsString(
                    (new LinkResponse(100L, URI.create("test.com")))
                ))));
        server.start();
        ScrapperClient scrapperClient = new ScrapperClient(makeClient());
        AddLinkRequest addLinkRequest = new AddLinkRequest(URI.create("test.com"));
        LinkResponse actual = new LinkResponse(100L, URI.create("test.com"));
        //Act
        LinkResponse response = scrapperClient.addLink(100L, addLinkRequest);
        //Assert
        assertThat(response).isEqualTo(actual);
    }

    @Test
    @DisplayName("Scrapper client request to LinkController removeLink test")
    @SneakyThrows
    public void removeLink_shouldSendRequestToLinkController() {
        //Arrange
        server = new WireMockServer(8080);
        server.stubFor(delete(urlPathMatching(LINK))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(objectMapper.writeValueAsString(
                    (new LinkResponse(100L, URI.create("test.com")))
                ))));
        server.start();
        ScrapperClient scrapperClient = new ScrapperClient(makeClient());
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(1L);
        LinkResponse actual = new LinkResponse(100L, URI.create("test.com"));
        //Act
        LinkResponse response = scrapperClient.removeLink(1L, removeLinkRequest);
        //Assert
        assertThat(response).isEqualTo(actual);
    }

    //Arrange
    private WebClient makeClient() {
        return WebClient
            .builder()
            .baseUrl("http://localhost:8080")
            .build();
    }

    @AfterEach
    public void teardown() {
        server.stop();
    }
}
