package edu.java.repository.link;

import edu.java.client.dto.LinkInfo;
import edu.java.dto.LinkData;
import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import edu.java.repository.link.mapper.LinkDataMapper;
import edu.java.repository.link.mapper.LinkResponseMapper;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Log4j2
public class JdbcLinkRepository implements LinkRepository {
    private final LinkResponseMapper mapper = new LinkResponseMapper();
    private final LinkDataMapper dataMapper = new LinkDataMapper();

    private final JdbcTemplate jdbcTemplate;

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
        Long linkId = jdbcTemplate.queryForObject(
            "INSERT INTO link (url) VALUES (?) RETURNING link_id",
            Long.class,
            addLinkRequest.link().toString()
        );
        return new LinkResponse(linkId, addLinkRequest.link());
    }

    @Override
    public LinkResponse remove(Long chatId, RemoveLinkRequest removeLinkRequest) {
        long id = removeLinkRequest.id();
        LinkResponse linkResponse = jdbcTemplate.queryForObject("SELECT * FROM link WHERE link_id = (?)", mapper, id);
        jdbcTemplate.update("DELETE FROM link WHERE link_id = (?)", id);
        return linkResponse;
    }

    @Override
    public void updateLink(LinkInfo info) {
        OffsetDateTime curTime = OffsetDateTime.now();
        jdbcTemplate.update("UPDATE link SET last_update_time = (?), name = (?), checked_at = (?)"
            + " WHERE url = (?)", info.lastActivityDate(), info.title(), curTime, info.url().toString());
    }

    @Override
    public LinkData getData(Long linkId) {
        return jdbcTemplate.queryForObject("SELECT last_update_time, url FROM link"
            + " WHERE link_id = (?)", dataMapper, linkId);
    }

    @Override
    public Long getLinkId(String url) {
        Boolean isInTable =
            jdbcTemplate.queryForObject("SELECT COUNT(link_id) FROM link WHERE url = (?)", Boolean.class, url);
        if (Boolean.TRUE.equals(isInTable)) {
            return jdbcTemplate.queryForObject("SELECT link_id FROM link WHERE url = (?)", Long.class, url);
        }
        return 0L;
    }
}
