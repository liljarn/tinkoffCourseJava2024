package edu.java.service.link;

import edu.java.client.ClientInfoProvider;
import edu.java.dto.ChatLinkResponse;
import edu.java.dto.LinkData;
import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import edu.java.exceptions.ChatNotAuthorizedException;
import edu.java.exceptions.LinkAlreadyTrackedException;
import edu.java.exceptions.LinkNotFoundException;
import edu.java.exceptions.LinkNotSupportedException;
import edu.java.repository.chat.ChatRepository;
import edu.java.repository.chat_link.ChatLinkRepository;
import edu.java.repository.link.LinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class DefaultLinkService implements LinkService {
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final ChatRepository chatRepository;
    private final List<ClientInfoProvider> clients;

    @Override
    @Transactional(readOnly = true)
    public ListLinksResponse getAllLinks(Long chatId) {
        return linkRepository.findAll(chatId);
    }

    @Override
    @Transactional
    public LinkResponse addLink(Long chatId, AddLinkRequest addLinkRequest) {
        if (!chatRepository.isInTable(chatId)) {
            throw new ChatNotAuthorizedException();
        }
        for (ClientInfoProvider client : clients) {
            if (client.isValidated(addLinkRequest.link())) {
                Long linkId = linkRepository.getLinkId(addLinkRequest.link().toString());
                if (linkId == 0) {
                    client.fetchData(addLinkRequest.link());
                    LinkResponse response = linkRepository.add(addLinkRequest);
                    chatLinkRepository.add(chatId, response.id());
                    return response;
                }
                if (chatLinkRepository.isTracked(chatId, linkId)) {
                    throw new LinkAlreadyTrackedException();
                }
                chatLinkRepository.add(chatId, linkId);
                return new LinkResponse(linkId, linkRepository.getData(linkId).url());
            }
        }
        throw new LinkNotSupportedException(addLinkRequest.link());
    }

    @Override
    @Transactional
    public LinkResponse deleteLink(Long chatId, RemoveLinkRequest removeLinkRequest) {
        if (chatLinkRepository.isTracked(chatId, removeLinkRequest.id())) {
            LinkResponse response = chatLinkRepository.remove(chatId, removeLinkRequest.id());
            if (!chatLinkRepository.hasChats(removeLinkRequest.id())) {
                return linkRepository.remove(removeLinkRequest);
            }
            return response;
        }
        throw new LinkNotFoundException();
    }

    @Override
    @Transactional
    public List<ChatLinkResponse> findAllChatsByLinksFiltered(OffsetDateTime time) {
        return chatLinkRepository.findAllFiltered(time);
    }

    @Override
    @Transactional
    public LinkData getData(Long linkId) {
        return linkRepository.getData(linkId);
    }

    @Override
    @Transactional
    public void updateLink(URI url, OffsetDateTime checkTime, OffsetDateTime lastUpdateTime) {
        linkRepository.updateLink(url, checkTime, lastUpdateTime);
    }
}
