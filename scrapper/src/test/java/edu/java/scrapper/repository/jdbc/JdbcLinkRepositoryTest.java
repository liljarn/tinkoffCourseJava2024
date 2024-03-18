package edu.java.scrapper.repository.jdbc;

import edu.java.dto.LinkData;
import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import edu.java.repository.link.LinkRepository;
import edu.java.scrapper.IntegrationEnvironment;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class JdbcLinkRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private LinkRepository linkRepository;

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jdbc");
    }

    @Test
    @Transactional
    @Rollback
    public void findAll_shouldReturnListLinksResponseWithEmptyList_whenChatDoesNotTrackAnyLink() {
        //Arrange
        Long chatId = 1L;
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (1)");
        //Act
        ListLinksResponse list = linkRepository.findAll(chatId);
        //Assert
        assertThat(list.links().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void findAll_shouldReturnListLinksResponseWithLinks_whenChatTracksLinks() {
        //Arrange
        Long chatId = 1L;
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (1)");
        String url = "google.com";
        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url) VALUES (?) RETURNING link_id", Long.class, url);
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId, linkId);
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
    public void add_shouldCorrectlyAddLinkInLinkTable_thenReturnLinkResponse() {
        //Arrange
        String url = "google.com";
        AddLinkRequest addLinkRequest = new AddLinkRequest(URI.create(url));
        //Act
        LinkResponse response = linkRepository.add(addLinkRequest);
        //Assert
        Long count =
            jdbcTemplate.queryForObject("SELECT COUNT(link_id) FROM link WHERE url = ?", Long.class, url);
        assertThat(count).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    public void remove_shouldCorrectlyRemoveLinkFromLinkTable_thenReturnLinkResponse() {
        //Arrange
        String url = "google.com";
        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url) VALUES (?) RETURNING link_id", Long.class, url);
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        LinkResponse expectedResponse = new LinkResponse(linkId, URI.create(url));
        //Act
        LinkResponse response = linkRepository.remove(removeLinkRequest);
        //Assert
        Long count =
            jdbcTemplate.queryForObject("SELECT COUNT(link_id) FROM link WHERE link_id = ?", Long.class, linkId);
        assertThat(response).isEqualTo(expectedResponse);
        assertThat(count).isEqualTo(0L);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    public void updateLink_shouldCorrectlyUpdateDataInDb_whenLinkInTable() {
        //Arrange
        String url = "google.com";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        OffsetDateTime time = OffsetDateTime.now();
        String expectedTime = time.format(formatter);
        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url) VALUES (?) RETURNING link_id", Long.class, url);
        //Act
        linkRepository.updateLink(URI.create(url), time, time);
        //Assert
        OffsetDateTime update = jdbcTemplate.queryForObject(
            "SELECT last_update_time FROM link WHERE url = (?)",
            OffsetDateTime.class,
            url
        );
        assertThat(update.atZoneSameInstant(ZoneId.systemDefault()).format(formatter)).isEqualTo(expectedTime);
    }

    @Test
    @Transactional
    @Rollback
    public void getData_shouldReturnDataFromDb_whenLinkExists() {
        //Arrange
        String url = "google.com";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        OffsetDateTime time = OffsetDateTime.now();
        String expectedTime = time.format(formatter);
        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url) VALUES (?) RETURNING link_id", Long.class, url);
        jdbcTemplate.update("UPDATE link SET last_update_time = (?) WHERE url = (?)", time, url);
        //Act
        LinkData response = linkRepository.getData(linkId);
        //Assert
        assertThat(response).extracting(update -> update.updateTime().format(formatter)).isEqualTo(expectedTime);
    }

    @Test
    @Transactional
    @Rollback
    public void getLinkId_shouldReturnLinkIdByURL_whenLinkIsInTable() {
        //Arrange
        String url = "google.com";
        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url) VALUES (?) RETURNING link_id", Long.class, url);
        //Act
        Long response = linkRepository.getLinkId(url);
        //Assert
        assertThat(response).isEqualTo(linkId);
    }

    @Test
    @Transactional
    @Rollback
    public void getLinkId_shouldReturnZero_whenLinkIsNotInTable() {
        //Arrange
        String url = "google.com";
        String notInTableUrl = "youtube.com";
        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url) VALUES (?) RETURNING link_id", Long.class, url);
        //Act
        Long response = linkRepository.getLinkId(notInTableUrl);
        //Assert
        assertThat(response).isEqualTo(0L);
    }
}
