package edu.java.repository.link;

import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;

public interface LinkRepository {
    ListLinksResponse findAll(Long chatId);

    LinkResponse add(Long chatId, AddLinkRequest addLinkRequest);

    LinkResponse remove(Long chatId, RemoveLinkRequest removeLinkRequest);
}
