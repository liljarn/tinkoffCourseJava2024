package edu.java.service;

import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import edu.java.repository.link.LinkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class LinkServiceImpl implements LinkService {
    private final LinkRepository linkRepository;

    @Override
    public ListLinksResponse getAllLinks(Long chatId) {
        log.info("All links were got successfully!");
        return linkRepository.findAll(chatId);
    }

    @Override
    public LinkResponse addLink(Long chatId, AddLinkRequest addLinkRequest) {
        log.info("Link was added successfully!");
        linkRepository.add(chatId, addLinkRequest);
        return new LinkResponse(chatId, addLinkRequest.link());
    }

    @Override
    public LinkResponse deleteLink(Long chatId, RemoveLinkRequest removeLinkRequest) {
        log.info("Link was removed successfully!");
        return linkRepository.remove(chatId, removeLinkRequest);
    }
}
