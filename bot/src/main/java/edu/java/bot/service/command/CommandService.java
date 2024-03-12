package edu.java.bot.service.command;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.dto.client.AddLinkRequest;
import edu.java.bot.dto.client.LinkResponse;
import edu.java.bot.dto.client.ListLinksResponse;
import edu.java.bot.dto.client.RemoveLinkRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandService {
    private final ScrapperClient client;

    public ListLinksResponse getLinks(Long chatId) {
        return client.getLinks(chatId);
    }

    public void registerChat(Long chatId) {
        client.registerChat(chatId);
    }

    public LinkResponse addLink(Long chatId, AddLinkRequest addLinkRequest) {
        return client.addLink(chatId, addLinkRequest);
    }

    public LinkResponse removeLink(Long chatId, RemoveLinkRequest removeLinkRequest) {
        return client.removeLink(chatId, removeLinkRequest);
    }
}
