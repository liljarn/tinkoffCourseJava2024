package edu.java.controller;

import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import edu.java.service.link.LinkService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
public class LinkController {
    private final LinkService linkService;

    private static final String TG_CHAT_ID = "Tg-Chat-Id";

    @GetMapping
    public ListLinksResponse getAllLinks(@RequestHeader(TG_CHAT_ID) @NotNull Long chatId) {
        return linkService.getAllLinks(chatId);
    }

    @PostMapping
    public LinkResponse addLink(
        @RequestHeader(TG_CHAT_ID)  @NotNull Long chatId,
        @RequestBody @Valid AddLinkRequest addLinkRequest
    ) {
        return linkService.addLink(chatId, addLinkRequest);
    }

    @DeleteMapping
    public LinkResponse deleteLink(
        @RequestHeader(TG_CHAT_ID) @NotNull Long chatId,
        @RequestBody @Valid RemoveLinkRequest removeLinkRequest
    ) {
        return linkService.deleteLink(chatId, removeLinkRequest);
    }
}
