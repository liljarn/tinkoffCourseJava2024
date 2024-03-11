package edu.java.repository.chat_link;

import edu.java.dto.ChatLinkResponse;
import edu.java.exceptions.LinkAlreadyTrackedException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcChatLinkRepository implements ChatLinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ChatLinkExtractor extractor = new ChatLinkExtractor();

    @Override
    public List<ChatLinkResponse> findAll() {
        return jdbcTemplate.query("SELECT link_id, chat_id FROM chat_link", extractor);
    }

    @Override
    public void add(Long chatId, Long linkId) {
        if (isTracked(chatId, linkId)) {
            throw new LinkAlreadyTrackedException();
        }
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId, linkId);
    }

    @Override
    public boolean delete(Long chatId, Long linkId) {
        jdbcTemplate.update("DELETE FROM chat_link WHERE link_id = (?) AND chat_id = (?)", linkId, chatId);
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM chat_link WHERE link_id = (?)", Boolean.class, linkId);
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
