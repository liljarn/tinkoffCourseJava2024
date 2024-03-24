package edu.java.repository.chat_link;

import edu.java.dto.ChatLinkResponse;
import edu.java.dto.response.LinkResponse;
import edu.java.repository.link.mapper.LinkResponseMapper;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JdbcChatLinkRepository implements ChatLinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ChatLinkExtractor extractor = new ChatLinkExtractor();

    @Override
    public List<ChatLinkResponse> findAllFiltered(OffsetDateTime time) {
        return jdbcTemplate.query(
            "SELECT link.link_id, link.checked_at, chat_link.chat_id"
            + " from link JOIN chat_link ON chat_link.link_id = link.link_id"
            + " WHERE checked_at < ? LIMIT 10",
            extractor,
            time
        );
    }

    @Override
    public void add(Long chatId, Long linkId) {
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId, linkId);
    }

    @Override
    public LinkResponse remove(Long chatId, Long linkId) {
        LinkResponse response =
            jdbcTemplate.queryForObject(
                "SELECT link.link_id, link.url from link "
                + "JOIN chat_link ON chat_link.link_id = link.link_id "
                + "WHERE chat_link.chat_id = (?) AND link.link_id = (?)",
                new LinkResponseMapper(),
                chatId,
                linkId
            );
        jdbcTemplate.update("DELETE FROM chat_link WHERE link_id = (?) AND chat_id = (?)", linkId, chatId);
        return response;
    }

    @Override
    public boolean isTracked(Long chatId, Long linkId) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM chat_link WHERE link_id = (?) AND chat_id = (?)",
            Boolean.class,
            linkId,
            chatId
        ));
    }

    @Override
    public boolean hasChats(Long linkId) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM chat_link WHERE link_id = (?)",
            Boolean.class,
            linkId
        ));
    }
}
