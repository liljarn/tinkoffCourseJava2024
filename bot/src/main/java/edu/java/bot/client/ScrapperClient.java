package edu.java.bot.client;

import edu.java.bot.dto.client.AddLinkRequest;
import edu.java.bot.dto.client.LinkResponse;
import edu.java.bot.dto.client.ListLinksResponse;
import edu.java.bot.dto.client.RemoveLinkRequest;
import edu.java.bot.dto.response.ApiErrorResponse;
import edu.java.bot.exception.ScrapperException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ScrapperClient {
    private static final String CHAT_ENDPOINT = "/tg-chat";
    private static final String LINK_ENDPOINT = "/links";
    private static final String TG_CHAT_HEADER = "Tg-Chat-Id";

    private final WebClient webClient;

    public ListLinksResponse getLinks(Long chatId) {
        return webClient
            .get()
            .uri(LINK_ENDPOINT)
            .header(TG_CHAT_HEADER, chatId.toString())
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::handleError)
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public LinkResponse addLink(Long chatId, AddLinkRequest addLinkRequest) {
        return webClient
            .post()
            .uri(LINK_ENDPOINT)
            .header(TG_CHAT_HEADER, chatId.toString())
            .bodyValue(addLinkRequest)
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::handleError)
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse removeLink(Long chatId, RemoveLinkRequest removeLinkRequest) {
        return webClient
            .method(HttpMethod.DELETE)
            .uri(LINK_ENDPOINT)
            .header(TG_CHAT_HEADER, chatId.toString())
            .bodyValue(removeLinkRequest)
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::handleError)
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public void registerChat(Long chatId) {
        webClient
            .post()
            .uri(CHAT_ENDPOINT + "/" + chatId.toString())
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::handleError)
            .bodyToMono(Void.class)
            .block();
    }

    public void deleteChat(Long chatId) {
        webClient
            .delete()
            .uri(CHAT_ENDPOINT + "/" + chatId)
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::handleError)
            .bodyToMono(Void.class)
            .block();
    }

    private Mono<? extends Throwable> handleError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(ApiErrorResponse.class)
            .flatMap(apiErrorResponse -> Mono.error(new ScrapperException(
                apiErrorResponse.description(),
                HttpStatus.valueOf(Integer.parseInt(apiErrorResponse.code())),
                apiErrorResponse.exceptionMessage()
            )));
    }
}
