package edu.java.repository.link;

import edu.java.dto.LinkData;
import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import java.net.URI;
import java.time.OffsetDateTime;

public interface LinkRepository {
    ListLinksResponse findAll(Long chatId);

    LinkResponse add(AddLinkRequest addLinkRequest);

    LinkResponse remove(RemoveLinkRequest removeLinkRequest);

    LinkData getData(Long linkId);

    void updateLink(URI url, OffsetDateTime checkTime, OffsetDateTime lastUpdateTime);

    Long getLinkId(String url);
}
