package edu.java.repository.link;

import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import edu.java.exceptions.LinkAlreadyTrackedException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final LinkResponseMapper mapper = new LinkResponseMapper();

    @Override
    public ListLinksResponse findAll(Long chatId) {
        List<LinkResponse> linksList =
            jdbcTemplate.query(
                "SELECT link.* FROM link "
                    + "JOIN chat_link ON link.link_id = chat_link.link_id WHERE chat_link.chat_id = ?",
                mapper,
                chatId
            );
        return new ListLinksResponse(linksList, linksList.size());
    }

    @Override
    public LinkResponse add(Long chatId, AddLinkRequest addLinkRequest) {
        String stringUrl = addLinkRequest.link().toString();
        Long linkId = getLinkId(stringUrl);
        if (linkId == 0) {
            linkId = jdbcTemplate.queryForObject(
                "INSERT INTO link (url) VALUES (?) RETURNING link_id",
                Long.class,
                stringUrl
            );
        }
        if (isTracked(chatId, linkId)) {
            throw new LinkAlreadyTrackedException();
        }
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId, linkId);
        return jdbcTemplate.queryForObject("SELECT * FROM link WHERE url = (?)", mapper, stringUrl);
    }

    @Override
    public LinkResponse remove(Long chatId, RemoveLinkRequest removeLinkRequest) {
        long id = removeLinkRequest.id();
        LinkResponse linkResponse = jdbcTemplate.queryForObject("SELECT * FROM link WHERE link_id = (?)", mapper, id);
        jdbcTemplate.update("DELETE FROM chat_link WHERE link_id = (?) AND chat_id = (?)", id, chatId);
        Long trackedLinks =
            jdbcTemplate.queryForObject("SELECT COUNT(*) FROM chat_link WHERE link_id = (?)", Long.class, id);
        if (trackedLinks == 0) {
            jdbcTemplate.update("DELETE FROM link WHERE link_id = (?)", id);
        }
        return linkResponse;
    }

    private Long getLinkId(String stringUrl) {
        Boolean isInTable =
            jdbcTemplate.queryForObject("SELECT COUNT(*) FROM link WHERE url = (?)", Boolean.class, stringUrl);
        if (isInTable) {
            return jdbcTemplate.queryForObject("SELECT link_id FROM link WHERE url = (?)", Long.class, stringUrl);
        }
        return 0L;
    }

    private boolean isTracked(Long chatId, Long linkId) {
        return jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM chat_link WHERE link_id = (?) AND chat_id = (?)",
            Boolean.class,
            linkId,
            chatId
        );
    }
}
