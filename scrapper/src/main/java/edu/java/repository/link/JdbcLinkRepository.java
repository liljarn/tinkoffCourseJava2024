package edu.java.repository.link;

import edu.java.client.dto.LinkInfo;
import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import edu.java.repository.chat_link.ChatLinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final LinkResponseMapper mapper = new LinkResponseMapper();
    private final ChatLinkRepository chatLinkRepository;

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
        chatLinkRepository.add(chatId, linkId);
        return jdbcTemplate.queryForObject("SELECT * FROM link WHERE url = (?)", mapper, stringUrl);
    }

    @Override
    public LinkResponse remove(Long chatId, RemoveLinkRequest removeLinkRequest) {
        long id = removeLinkRequest.id();
        LinkResponse linkResponse = jdbcTemplate.queryForObject("SELECT * FROM link WHERE link_id = (?)", mapper, id);
        if (!chatLinkRepository.delete(chatId, id)) {
            jdbcTemplate.update("DELETE FROM link WHERE link_id = (?)", id);
        }
        return linkResponse;
    }

    @Override
    public URI findLink(Long linkId) {
        return jdbcTemplate.queryForObject("SELECT url FROM link WHERE link_id = (?)", URI.class, linkId);
    }

    @Override
    public void updateLink(LinkInfo info) {
        jdbcTemplate.update("UPDATE link SET last_update_time = (?), name = (?)"
            + " WHERE url = (?)", info.lastActivityDate(), info.title(), info.url().toString());
    }

    @Override
    public OffsetDateTime getDate(Long linkId) {
        return jdbcTemplate.queryForObject("SELECT last_update_time FROM link"
            + " WHERE link_id = (?)", OffsetDateTime.class, linkId);
    }

    private Long getLinkId(String stringUrl) {
        Boolean isInTable =
            jdbcTemplate.queryForObject("SELECT COUNT(*) FROM link WHERE url = (?)", Boolean.class, stringUrl);
        if (isInTable) {
            return jdbcTemplate.queryForObject("SELECT link_id FROM link WHERE url = (?)", Long.class, stringUrl);
        }
        return 0L;
    }
}
