package edu.java.scrapper.repository.jdbc;

import edu.java.dto.ChatLinkResponse;
import edu.java.repository.chat_link.ChatLinkRepository;
import edu.java.scrapper.IntegrationEnvironment;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
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
public class JdbcChatLinkRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ChatLinkRepository repository;

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jdbc");
    }

    @Transactional
    @Rollback
    @Test
    public void hasChats_shouldReturnTrue_whenFindAnyChatIdByLinkId() {
        //Arrange
        long chatId = 1L;
        String url = "google.com";
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url) VALUES (?) RETURNING link_id", Long.class, url);
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
        String url = "google.com";
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url) VALUES (?) RETURNING link_id", Long.class, url);
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
        String url = "google.com";
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", secondChatId);
        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url) VALUES (?) RETURNING link_id", Long.class, url);
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
        String url = "google.com";
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url) VALUES (?) RETURNING link_id", Long.class, url);
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
        String url = "google.com";
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url) VALUES (?) RETURNING link_id", Long.class, url);
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
    public void findAllFiltered_shouldReturn_whenIdExistsInTables() {
        //Arrange
        long chatId = 1L;
        long secondChatId = 2L;
        String url = "google.com";
        String secondUrl = "yandex.ru";
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", secondChatId);
        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url) VALUES (?) RETURNING link_id", Long.class, url);
        Long secondLinkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url) VALUES (?) RETURNING link_id", Long.class, secondUrl);
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId, linkId);
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", secondChatId, linkId);
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId, secondLinkId);
        List<ChatLinkResponse> expected = List.of(
            new ChatLinkResponse(linkId, Set.of(chatId, secondChatId)),
            new ChatLinkResponse(secondLinkId, Set.of(chatId))
        );
        OffsetDateTime time = OffsetDateTime.now();
        time = time.minusMinutes(5);
        //Act
        List<ChatLinkResponse> response = repository.findAllFiltered(time);
        //Assert
        assertThat(response).isEqualTo(expected);
    }
}
