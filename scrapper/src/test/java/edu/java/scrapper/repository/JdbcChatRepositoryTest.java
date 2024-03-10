package edu.java.scrapper.repository;

import edu.java.exceptions.ChatAlreadyRegisteredException;
import edu.java.exceptions.ChatNotFoundException;
import edu.java.repository.chat.ChatRepository;
import edu.java.repository.chat.JdbcChatRepository;
import edu.java.scrapper.IntegrationEnvironment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JdbcChatRepositoryTest extends IntegrationEnvironment {
    private static JdbcTemplate jdbcTemplate;

    //Arrange
    @BeforeAll
    public static void setup() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(POSTGRES.getDriverClassName());
        dataSource.setUrl(POSTGRES.getJdbcUrl());
        dataSource.setUsername(POSTGRES.getUsername());
        dataSource.setPassword(POSTGRES.getPassword());
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    @Transactional
    @Rollback
    public void add_shouldCorrectlyAddIdInChatTable_whenIdIsNotInTable() {
        //Arrange
        ChatRepository chatRepository = new JdbcChatRepository(jdbcTemplate);
        Long chatId = 1L;
        //Act
        chatRepository.add(chatId);
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM chat WHERE chat_id = ?", Long.class, chatId);
        //Assert
        assertThat(count).isEqualTo(1L);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    public void add_shouldThrowChatAlreadyRegisteredException_whenIdIsAlreadyInTable() {
        ChatRepository chatRepository = new JdbcChatRepository(jdbcTemplate);
        Connection connection = POSTGRES.createConnection("");
        long chatId = 112003L;
        PreparedStatement statement = connection.prepareStatement("INSERT INTO chat (chat_id) VALUES (?)");
        statement.setLong(1, chatId);
        statement.executeUpdate();
        assertThatThrownBy(() -> chatRepository.add(chatId)).isInstanceOf(ChatAlreadyRegisteredException.class);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    public void remove_shouldDeleteIdFromChatTable_whenIdExists() {
        ChatRepository chatRepository = new JdbcChatRepository(jdbcTemplate);
        Connection connection = POSTGRES.createConnection("");
        Long chatId = 1879L;
        PreparedStatement statement = connection.prepareStatement("INSERT INTO chat (chat_id) VALUES (?)");
        statement.setLong(1, chatId);
        statement.executeUpdate();
        chatRepository.remove(chatId);
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM chat WHERE chat_id = ?", Long.class, chatId);
        assertThat(count).isEqualTo(0L);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    public void remove_shouldThrowChatNotFoundException_whenChatIdDoesNotExist() {
        ChatRepository chatRepository = new JdbcChatRepository(jdbcTemplate);
        Long chatId = 1879L;
        assertThatThrownBy(() -> chatRepository.remove(chatId)).isInstanceOf(ChatNotFoundException.class);
    }
}
