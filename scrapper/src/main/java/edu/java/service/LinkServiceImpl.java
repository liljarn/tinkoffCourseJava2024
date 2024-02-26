package edu.java.service;

import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import java.util.Collections;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class LinkServiceImpl implements LinkService {
    @Override
    public ListLinksResponse getAllLinks(Long chatId) {
        log.info("All links were got successfully!");
        return new ListLinksResponse(Collections.emptyList(), 0);
    }

    @Override
    public LinkResponse addLink(Long chatId, AddLinkRequest addLinkRequest) {
        log.info("Link was added successfully!");
        return new LinkResponse(chatId, addLinkRequest.link());
    }

    @Override
    public LinkResponse deleteLink(Long chatId, RemoveLinkRequest removeLinkRequest) {
        log.info("Link was removed successfully!");
        return new LinkResponse(chatId, removeLinkRequest.link());
    }
}
