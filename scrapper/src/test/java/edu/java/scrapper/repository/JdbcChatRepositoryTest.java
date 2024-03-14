package edu.java.scrapper.repository;

import edu.java.repository.chat.ChatRepository;
import edu.java.repository.chat.JdbcChatRepository;
import edu.java.scrapper.IntegrationEnvironment;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

public class JdbcChatRepositoryTest extends IntegrationEnvironment {
    private static JdbcTemplate jdbcTemplate;
    private static ChatRepository chatRepository;

    //Arrange
    @BeforeAll
    public static void setup() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(POSTGRES.getDriverClassName());
        dataSource.setUrl(POSTGRES.getJdbcUrl());
        dataSource.setUsername(POSTGRES.getUsername());
        dataSource.setPassword(POSTGRES.getPassword());
        jdbcTemplate = new JdbcTemplate(dataSource);
        chatRepository = new JdbcChatRepository(jdbcTemplate);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
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
    @SneakyThrows
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
    @SneakyThrows
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

    @AfterAll
    public static void teardown() {
        POSTGRES.close();
    }
}
