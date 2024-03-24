package edu.java.service.link;

import edu.java.dto.ChatLinkResponse;
import edu.java.dto.LinkData;
import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService {
    ListLinksResponse getAllLinks(Long chatId);

    LinkResponse addLink(Long chatId, AddLinkRequest addLinkRequest);

    LinkResponse deleteLink(Long chatId, RemoveLinkRequest addLinkRequest);

    List<ChatLinkResponse> findAll(OffsetDateTime time);

    LinkData getData(Long linkId);

    void updateLink(URI url, OffsetDateTime checkTime, OffsetDateTime lastUpdateTime);
}
