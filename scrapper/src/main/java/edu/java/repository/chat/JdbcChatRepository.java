package edu.java.repository.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(Long chatId) {
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
    }

    @Override
    public void remove(Long chatId) {
        jdbcTemplate.update("DELETE FROM chat WHERE chat_id = (?)", chatId);
    }

    @Override
    public boolean isInTable(Long chatId) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM chat WHERE chat_id = ?",
            Boolean.class,
            chatId
        ));
    }
}
