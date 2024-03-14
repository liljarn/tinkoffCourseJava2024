package edu.java.scrapper.repository;

import edu.java.dto.ChatLinkResponse;
import edu.java.repository.chat_link.ChatLinkRepository;
import edu.java.repository.chat_link.JdbcChatLinkRepository;
import edu.java.scrapper.IntegrationEnvironment;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

public class JdbcChatLinkRepositoryTest extends IntegrationEnvironment {
    private static JdbcTemplate jdbcTemplate;
    private static ChatLinkRepository repository;

    //Arrange
    @BeforeAll
    public static void setup() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(POSTGRES.getDriverClassName());
        dataSource.setUrl(POSTGRES.getJdbcUrl());
        dataSource.setUsername(POSTGRES.getUsername());
        dataSource.setPassword(POSTGRES.getPassword());
        jdbcTemplate = new JdbcTemplate(dataSource);
        repository = new JdbcChatLinkRepository(jdbcTemplate);
    }

    @Transactional
    @Rollback
    @Test
    public void hasChats_shouldReturnTrue_whenFindAnyChatIdByLinkId() {
        //Arrange
        long chatId = 1L;
        long linkId = 1L;
        String url = "google.com";
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        jdbcTemplate.update("INSERT INTO link (url) VALUES (?)", url);
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId, linkId);
        //Act
        boolean response = repository.hasChats(linkId);
        //Assert
        assertThat(response).isTrue();
    }

    @Transactional
    @Rollback
    @Test
    public void hasChats_shouldReturnFalse_whenDoesNotFindAnyChatIdByLinkId() {
        //Arrange
        long linkId = 1L;
        //Act
        boolean response = repository.hasChats(linkId);
        //Assert
        assertThat(response).isFalse();
    }

    @Transactional
    @Rollback
    @Test
    public void isTracked_shouldReturnTrue_whenDbHasLinkIdAndChatId() {
        //Arrange
        long chatId = 1L;
        long linkId = 1L;
        String url = "google.com";
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        jdbcTemplate.update("INSERT INTO link (url) VALUES (?)", url);
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId, linkId);
        //Act
        boolean response = repository.isTracked(chatId, linkId);
        //Assert
        assertThat(response).isTrue();
    }

    @Transactional
    @Rollback
    @Test
    public void isTracked_shouldReturnFalse_whenDbHasNotLinkIdAndChatId() {
        //Arrange
        long chatId = 1L;
        long secondChatId = 2L;
        long linkId = 1L;
        String url = "google.com";
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", secondChatId);
        jdbcTemplate.update("INSERT INTO link (url) VALUES (?)", url);
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId, linkId);
        //Act
        boolean response = repository.isTracked(secondChatId, linkId);
        //Assert
        assertThat(response).isFalse();
    }

    @Transactional
    @Rollback
    @Test
    public void add_shouldCorrectlyAddDataInChatLinkTable_whenIdExistsInTables() {
        //Arrange
        long chatId = 1L;
        long linkId = 1L;
        String url = "google.com";
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        jdbcTemplate.update("INSERT INTO link (url) VALUES (?)", url);
        //Act
        repository.add(chatId, linkId);
        //Assert
        Boolean hasBeenAdded = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM chat_link WHERE chat_id = ? AND link_id = ?",
            Boolean.class,
            chatId,
            linkId
        );
        assertThat(hasBeenAdded).isTrue();
    }

    @Transactional
    @Rollback
    @Test
    public void remove_shouldCorrectlyRemoveDataFromChatLinkTable_whenIdExistsInChatLinkTable() {
        //Arrange
        long chatId = 1L;
        long linkId = 1L;
        String url = "google.com";
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        jdbcTemplate.update("INSERT INTO link (url) VALUES (?)", url);
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId, linkId);
        //Act
        repository.remove(chatId, linkId);
        //Assert
        Boolean hasBeenRemoved = Boolean.FALSE.equals(jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM chat_link WHERE chat_id = ? AND link_id = ?",
            Boolean.class,
            chatId,
            linkId
        ));
        assertThat(hasBeenRemoved).isTrue();
    }

    @Transactional
    @Rollback
    @Test
    public void findAll_shouldReturn_whenIdExistsInTables() {
        //Arrange
        long chatId = 1L;
        long secondChatId = 2L;
        long linkId = 1L;
        long secondLinkId = 2L;
        String url = "google.com";
        String secondUrl = "yandex.ru";
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", secondChatId);
        jdbcTemplate.update("INSERT INTO link (url) VALUES (?)", url);
        jdbcTemplate.update("INSERT INTO link (url) VALUES (?)", secondUrl);
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId, linkId);
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", secondChatId, linkId);
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId, secondLinkId);
        List<ChatLinkResponse> expected = List.of(
            new ChatLinkResponse(linkId, Set.of(chatId, secondChatId)),
            new ChatLinkResponse(secondLinkId, Set.of(chatId))
        );
        //Act
        List<ChatLinkResponse> response = repository.findAll();
        //Assert
        assertThat(response).isEqualTo(expected);
    }

    @AfterEach
    void cleanUp() {
        jdbcTemplate.update("TRUNCATE TABLE chat_link, link, chat RESTART IDENTITY CASCADE");
    }

    @AfterAll
    public static void closeTestContainer() {
        POSTGRES.close();
    }
}
