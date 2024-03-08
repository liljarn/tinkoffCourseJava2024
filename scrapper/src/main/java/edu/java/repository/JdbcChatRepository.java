package edu.java.repository;

import edu.java.exceptions.ChatAlreadyRegisteredException;
import edu.java.exceptions.ChatNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(Long chatId) {
        if (isInTable(chatId)) {
            throw new ChatAlreadyRegisteredException();
        }
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
    }

    @Override
    public void remove(Long chatId) {
        if (!isInTable(chatId)) {
            throw new ChatNotFoundException(chatId);
        }
        jdbcTemplate.update("DELETE FROM chat WHERE chat_id = (?)", chatId);
    }

    @Override
    public void findAll() {

    }

    private boolean isInTable(Long chatId) {
        Long id = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM chat WHERE chat_id = ?", Long.class, chatId);
        return id != 0;
    }
}
