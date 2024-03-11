package edu.java.service.link.jdbc;

import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import edu.java.repository.link.LinkRepository;
import edu.java.service.link.LinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class JdbcLinkService implements LinkService {
    private final LinkRepository linkRepository;

    @Override
    @Transactional(readOnly = true)
    public ListLinksResponse getAllLinks(Long chatId) {
        log.info("All links were got successfully!");
        return linkRepository.findAll(chatId);
    }

    @Override
    @Transactional
    public LinkResponse addLink(Long chatId, AddLinkRequest addLinkRequest) {
        log.info("Link was added successfully!");
        return linkRepository.add(chatId, addLinkRequest);
    }

    @Override
    @Transactional
    public LinkResponse deleteLink(Long chatId, RemoveLinkRequest removeLinkRequest) {
        log.info("Link was removed successfully!");
        return linkRepository.remove(chatId, removeLinkRequest);
    }
}
