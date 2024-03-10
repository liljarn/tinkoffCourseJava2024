package edu.java.scrapper.repository;

import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import edu.java.exceptions.LinkAlreadyTrackedException;
import edu.java.repository.link.JdbcLinkRepository;
import edu.java.repository.link.LinkRepository;
import edu.java.scrapper.IntegrationEnvironment;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JdbcLinkRepositoryTest extends IntegrationEnvironment {
    private static JdbcTemplate jdbcTemplate;
    private static LinkRepository linkRepository;

    //Arrange
    @BeforeAll
    public static void setup() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(POSTGRES.getDriverClassName());
        dataSource.setUrl(POSTGRES.getJdbcUrl());
        dataSource.setUsername(POSTGRES.getUsername());
        dataSource.setPassword(POSTGRES.getPassword());
        jdbcTemplate = new JdbcTemplate(dataSource);
        linkRepository = new JdbcLinkRepository(jdbcTemplate);
    }

    @Test
    @Transactional
    @Rollback
    public void findAll_shouldReturnListLinksResponseWithEmptyList_whenChatDoesNotTrackAnyLink() {
        //Arrange
        Long chatId = 1L;
        //Act
        ListLinksResponse list = linkRepository.findAll(chatId);
        //Assert
        assertThat(list.links().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    public void findAll_shouldReturnListLinksResponseWithLinks_whenChatTracksLinks() {
        //Arrange
        Long chatId = 1L;
        Long linkId = 1L;
        String url = "google.com";
        Connection connection = POSTGRES.createConnection("");
        addChat(connection, chatId);
        PreparedStatement statement = connection.prepareStatement("INSERT INTO link (url) VALUES (?)");
        statement.setString(1, url);
        statement.executeUpdate();
        statement = connection.prepareStatement("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)");
        statement.setLong(1, chatId);
        statement.setLong(2, linkId);
        statement.addBatch();
        statement.executeBatch();

        ListLinksResponse expectedList =
            new ListLinksResponse(List.of(new LinkResponse(linkId, URI.create(url))), 1);
        //Act
        ListLinksResponse actualList = linkRepository.findAll(chatId);
        //Assert
        assertThat(actualList).isEqualTo(expectedList);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    public void add_shouldCorrectlyAddLinkInLinkTable() {
        //Arrange
        Long chatId = 1L;
        String url = "google.com";
        Connection connection = POSTGRES.createConnection("");
        addChat(connection, chatId);
        AddLinkRequest addLinkRequest = new AddLinkRequest(URI.create(url));
        LinkResponse expectedResponse = new LinkResponse(1L, URI.create(url));
        //Act
        LinkResponse response = linkRepository.add(chatId, addLinkRequest);
        //Assert
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(link_id) FROM link WHERE link_id = ?", Long.class, 1L);
        assertThat(count).isEqualTo(1);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    public void add_shouldThrowLinkAlreadyTrackedException_whenLinkIsAlreadyTracked() {
        //Arrange
        Long chatId = 1L;
        String url = "google.com";
        Connection connection = POSTGRES.createConnection("");
        addChat(connection, chatId);
        AddLinkRequest addLinkRequest = new AddLinkRequest(URI.create(url));
        //Act
        LinkResponse response = linkRepository.add(chatId, addLinkRequest);
        //Assert
        assertThatThrownBy(() -> linkRepository.add(
            chatId,
            addLinkRequest
        )).isInstanceOf(LinkAlreadyTrackedException.class);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    public void remove_shouldCorrectlyRemoveLinkFromLinkTable() {
        //Arrange
        Long chatId = 1L;
        Long linkId = 1L;
        String url = "google.com";
        Connection connection = POSTGRES.createConnection("");
        addChat(connection, chatId);
        PreparedStatement statement = connection.prepareStatement("INSERT INTO link (url) VALUES (?)");
        statement.setString(1, url);
        statement.executeUpdate();
        statement = connection.prepareStatement("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)");
        statement.setLong(1, chatId);
        statement.setLong(2, linkId);
        statement.addBatch();
        statement.executeBatch();
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        LinkResponse expectedResponse = new LinkResponse(linkId, URI.create(url));
        //Act
        LinkResponse response = linkRepository.remove(chatId, removeLinkRequest);
        //Assert
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(link_id) FROM link WHERE link_id = ?", Long.class, 1L);
        assertThat(response).isEqualTo(expectedResponse);
        assertThat(count).isEqualTo(0L);
    }

    @SneakyThrows
    private void addChat(Connection connection, Long chatId) {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO chat (chat_id) VALUES (?)");
        statement.setLong(1, chatId);
        statement.executeUpdate();
    }
}
