package edu.java.scrapper.repository.jooq;

import edu.java.repository.chat.ChatRepository;
import edu.java.scrapper.IntegrationEnvironment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqChatRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jooq");
    }

    @Test
    @Transactional
    @Rollback
    public void add_shouldCorrectlyAddIdInChatTable_whenIdIsNotInTable() {
        //Arrange
        long chatId = 1L;
        //Act
        chatRepository.add(chatId);
        //Assert
        Long idCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM chat WHERE chat_id = ?", Long.class, chatId);
        assertThat(idCount).isEqualTo(1L);
    }

    @Test
    @Transactional
    @Rollback
    public void remove_shouldDeleteIdFromChatTable_whenIdExists() {
        //Arrange
        long chatId = 1879L;
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        //Act
        chatRepository.remove(chatId);
        //Assert
        Long idCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM chat WHERE chat_id = ?", Long.class, chatId);
        assertThat(idCount).isEqualTo(0L);
    }

    @Test
    @Transactional
    @Rollback
    public void isInTable_shouldReturnTrue_whenChatIdExists() {
        //Arrange
        long chatId = 2000L;
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        //Act
        boolean inTable = chatRepository.isInTable(chatId);
        //Assert
        assertThat(inTable).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    public void isInTable_shouldReturnFalse_whenChatIdDoesNotExists() {
        //Arrange
        long chatId = 123L;
        //Act
        boolean inTable = chatRepository.isInTable(chatId);
        //Assert
        assertThat(inTable).isFalse();
    }
}
