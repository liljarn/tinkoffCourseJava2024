package edu.java.scrapper.service;

import edu.java.exceptions.ChatAlreadyRegisteredException;
import edu.java.exceptions.ChatNotFoundException;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.service.chat.ChatService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class JpaChatServiceTest extends IntegrationEnvironment {
    @Autowired
    private ChatService chatService;

    @Autowired
    private EntityManager manager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DynamicPropertySource
    static void jpaProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }

    @Test
    @Transactional
    @Rollback
    public void registerChat_shouldCorrectlyAddIdInChatTable_whenIdIsNotInTable() {
        //Arrange
        long chatId = 1L;
        //Act
        chatService.registerChat(chatId);
        manager.flush();
        //Assert
        Long idCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM chat WHERE chat_id = ?", Long.class, chatId);
        assertThat(idCount).isEqualTo(1L);
    }

    @Test
    @Transactional
    @Rollback
    public void registerChat_shouldThrowChatAlreadyRegisteredException_whenIdIsInTable() {
        //Arrange
        long chatId = 1L;
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        //Expected
        assertThatThrownBy(() -> chatService.registerChat(chatId)).isInstanceOf(ChatAlreadyRegisteredException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteChat_shouldDeleteIdFromChatTable_whenIdExists() {
        //Arrange
        long chatId = 1879L;
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        //Act
        chatService.deleteChat(chatId);
        manager.flush();
        //Assert
        Long idCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM chat WHERE chat_id = ?", Long.class, chatId);
        assertThat(idCount).isEqualTo(0L);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteChat_shouldThrowChatNotFoundException_whenIdDoesNotExist() {
        //Arrange
        long chatId = 1879L;
        //Expected
        assertThatThrownBy(() -> chatService.deleteChat(chatId)).isInstanceOf(ChatNotFoundException.class);
    }
}
