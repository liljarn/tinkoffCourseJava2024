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
import edu.java.repository.jpa.chat.JpaChatRepository;
import edu.java.repository.jpa.entity.ChatEntity;
import edu.java.repository.jpa.entity.LinkEntity;
import edu.java.repository.jpa.link.JpaLinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;
    private final List<ClientInfoProvider> clients;

    @Override
    @Transactional(readOnly = true)
    public ListLinksResponse getAllLinks(Long chatId) {
        List<LinkResponse> linkResponses = chatRepository
            .findById(chatId)
            .orElseThrow(ChatNotAuthorizedException::new)
            .getLinks()
            .stream()
            .map(linkEntity -> new LinkResponse(linkEntity.getLinkId(), URI.create(linkEntity.getUrl())))
            .toList();
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    @Override
    @Transactional
    public LinkResponse addLink(Long chatId, AddLinkRequest addLinkRequest) {
        ChatEntity chatEntity = chatRepository.findById(chatId).orElseThrow(ChatNotAuthorizedException::new);
        for (ClientInfoProvider client : clients) {
            if (client.isValidated(addLinkRequest.link())) {
                Optional<LinkEntity> optionalLinkEntity = linkRepository.findByUrl(addLinkRequest.link().toString());
                if (optionalLinkEntity.isEmpty()) {
                    client.fetchData(addLinkRequest.link());
                    LinkEntity link = linkRepository.save(new LinkEntity(addLinkRequest.link().toString()));
                    chatEntity.addLink(link);
                    return new LinkResponse(link.getLinkId(), URI.create(link.getUrl()));
                }
                LinkEntity link = optionalLinkEntity.get();
                if (chatEntity.getLinks().contains(link)) {
                    throw new LinkAlreadyTrackedException();
                }
                chatEntity.addLink(link);
                return new LinkResponse(link.getLinkId(), URI.create(link.getUrl()));
            }
        }
        throw new LinkNotSupportedException(addLinkRequest.link());
    }

    @Override
    @Transactional
    public LinkResponse deleteLink(Long chatId, RemoveLinkRequest addLinkRequest) {
        ChatEntity chatEntity = chatRepository.findById(chatId).orElseThrow(ChatNotAuthorizedException::new);
        Optional<LinkEntity> linkEntity = linkRepository.findById(addLinkRequest.id());
        if (linkEntity.isPresent() && chatEntity.getLinks().contains(linkEntity.get())) {
            LinkEntity link = linkEntity.get();
            LinkResponse response = new LinkResponse(link.getLinkId(), URI.create(link.getUrl()));
            chatEntity.getLinks().remove(link);
            link.getChats().remove(chatEntity);
            if (link.getChats().isEmpty()) {
                linkRepository.delete(link);
            }
            return response;
        }
        throw new LinkNotFoundException();
    }

    @Override
    @Transactional
    public List<ChatLinkResponse> findAllChatsByLinksFiltered(OffsetDateTime time) {
        Set<LinkEntity> links = linkRepository.findAllByCheckedAtBefore(time);
        return links.stream()
            .map(linkEntity -> new ChatLinkResponse(
                    linkEntity.getLinkId(),
                    linkEntity.getChats().stream().map(ChatEntity::getChatId).collect(Collectors.toSet())
                )
            )
            .toList();
    }

    @Override
    @Transactional
    public LinkData getData(Long linkId) {
        LinkEntity link = linkRepository.findById(linkId).orElseThrow(LinkNotFoundException::new);
        return new LinkData(link.getLastUpdateTime(), URI.create(link.getUrl()));
    }

    @Override
    @Transactional
    public void updateLink(URI url, OffsetDateTime checkTime, OffsetDateTime lastUpdateTime) {
        LinkEntity link = linkRepository.findByUrl(url.toString()).orElseThrow(LinkNotFoundException::new);
        link.setCheckedAt(checkTime);
        link.setLastUpdateTime(lastUpdateTime);
    }
}
