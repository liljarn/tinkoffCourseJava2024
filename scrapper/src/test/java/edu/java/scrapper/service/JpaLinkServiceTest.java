package edu.java.scrapper.service;

import edu.java.client.dto.LinkInfo;
import edu.java.client.github.GitHubInfoProvider;
import edu.java.dto.ChatLinkResponse;
import edu.java.dto.LinkData;
import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import edu.java.exceptions.ChatNotAuthorizedException;
import edu.java.exceptions.LinkAlreadyTrackedException;
import edu.java.exceptions.LinkNotFoundException;
import edu.java.exceptions.LinkNotSupportedException;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.service.link.LinkService;
import jakarta.persistence.EntityManager;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class JpaLinkServiceTest extends IntegrationEnvironment {
    @Autowired
    private LinkService linkService;

    @Autowired
    private EntityManager manager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private GitHubInfoProvider client;

    @DynamicPropertySource
    static void jpaProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }

    @Test
    @Transactional
    @Rollback
    public void getAllLinks_shouldReturnListLinksResponseWithEmptyList_whenChatDoesNotTrackAnyLink() {
        //Arrange
        Long chatId = 1L;
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (1)");
        //Act
        ListLinksResponse list = linkService.getAllLinks(chatId);
        //Assert
        assertThat(list.links().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void getAllLinks_shouldThrowChatNotAuthorizedException_whenChatIsNotInDb() {
        //Arrange
        Long chatId = 1L;
        //Expected
        assertThatThrownBy(() -> linkService.getAllLinks(chatId)).isInstanceOf(ChatNotAuthorizedException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void getAllLinks_shouldReturnListLinksResponseWithLinks_whenChatTracksLinks() {
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
        ListLinksResponse actualList = linkService.getAllLinks(chatId);
        //Assert
        assertThat(actualList).isEqualTo(expectedList);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteLink_shouldRemoveLinkFromLinkTable_whenNobodyTracksIt() {
        //Arrange
        String url = "google.com";
        Long chatId = 1L;
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url) VALUES (?) RETURNING link_id", Long.class, url);
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId, linkId);
        LinkResponse expectedResponse = new LinkResponse(linkId, URI.create(url));
        //Act
        LinkResponse response = linkService.deleteLink(chatId, removeLinkRequest);
        manager.flush();
        //Assert
        Long count =
            jdbcTemplate.queryForObject("SELECT COUNT(link_id) FROM link WHERE link_id = ?", Long.class, linkId);
        assertThat(response).isEqualTo(expectedResponse);
        assertThat(count).isEqualTo(0L);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteLink_shouldRemoveLinkOnlyFromChatLinkTable_whenLinkTrackedByMoreThanOneChat() {
        //Arrange
        String url = "google.com";
        Long chatId = 1L;
        Long secChatId = 2L;
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", secChatId);
        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url) VALUES (?) RETURNING link_id", Long.class, url);
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId, linkId);
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", secChatId, linkId);
        //Act
        linkService.deleteLink(chatId, removeLinkRequest);
        manager.flush();
        //Assert
        Long count =
            jdbcTemplate.queryForObject("SELECT COUNT(link_id) FROM link WHERE link_id = ?", Long.class, linkId);
        Long chatCount =
            jdbcTemplate.queryForObject(
                "SELECT COUNT(chat_id) FROM chat_link WHERE chat_id = ?",
                Long.class,
                chatId
            );
        assertThat(count).isEqualTo(1L);
        assertThat(chatCount).isEqualTo(0L);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteLink_shouldThrowChatNotAuthorizedException_whenChatIdDoesNotExist() {
        //Arrange
        Long chatId = 1L;
        Long linkId = 1L;
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        //Expected
        assertThatThrownBy(() -> linkService.deleteLink(
            chatId,
            removeLinkRequest
        )).isInstanceOf(ChatNotAuthorizedException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteLink_shouldThrowLinkNotFoundException_whenLinkIdIsNotInDb() {
        //Arrange
        Long chatId = 1L;
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        Long linkId = 1L;
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        //Expected
        assertThatThrownBy(() -> linkService.deleteLink(
            chatId,
            removeLinkRequest
        )).isInstanceOf(LinkNotFoundException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteLink_shouldThrowLinkNotFoundException_whenLinkIsNotTrackedByChat() {
        //Arrange
        String url = "google.com";
        Long chatId = 1L;
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url) VALUES (?) RETURNING link_id", Long.class, url);
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        //Expected
        assertThatThrownBy(() -> linkService.deleteLink(
            chatId,
            removeLinkRequest
        )).isInstanceOf(LinkNotFoundException.class);
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
        LinkData response = linkService.getData(linkId);
        manager.flush();
        //Assert
        assertThat(response).extracting(
                update -> update.updateTime()
                    .atZoneSameInstant(ZoneId.systemDefault())
                    .format(formatter)
            )
            .isEqualTo(expectedTime);
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
        jdbcTemplate.queryForObject("INSERT INTO link (url) VALUES (?) RETURNING link_id", Long.class, url);
        //Act
        linkService.updateLink(URI.create(url), time, time);
        manager.flush();
        //Assert
        OffsetDateTime update = jdbcTemplate.queryForObject(
            "SELECT last_update_time FROM link WHERE url = (?)",
            OffsetDateTime.class,
            url
        );
        assertThat(update.atZoneSameInstant(ZoneId.systemDefault()).format(formatter)).isEqualTo(expectedTime);
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
        List<ChatLinkResponse> response = linkService.findAllChatsByLinksFiltered(time);
        //Assert
        assertThat(response).isEqualTo(expected);
    }

    @Test
    @Transactional
    @Rollback
    public void addLink_shouldCorrectlyAddLinkInLinkTable_thenReturnLinkResponse() {
        //Arrange
        String url = "google.com";
        Mockito.when(client.isValidated(URI.create(url))).thenReturn(true);
        Mockito.when(client.fetchData(URI.create(url)))
            .thenReturn(List.of(new LinkInfo(URI.create(url), "title", OffsetDateTime.now())));
        Long chatId = 1L;
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        AddLinkRequest addLinkRequest = new AddLinkRequest(URI.create(url));
        //Act
        LinkResponse response = linkService.addLink(chatId, addLinkRequest);
        //Assert
        Long count =
            jdbcTemplate.queryForObject("SELECT COUNT(link_id) FROM link WHERE url = ?", Long.class, url);
        assertThat(count).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    public void addLink_shouldThrowLinkAlreadyTrackedException_whenLinkIsTracked() {
        //Arrange
        String url = "google.com";
        Mockito.when(client.isValidated(URI.create(url))).thenReturn(true);
        Mockito.when(client.fetchData(URI.create(url)))
            .thenReturn(List.of(new LinkInfo(URI.create(url), "title", OffsetDateTime.now())));
        Long chatId = 1L;
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url) VALUES (?) RETURNING link_id", Long.class, url);
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId, linkId);
        AddLinkRequest addLinkRequest = new AddLinkRequest(URI.create(url));
        //Expected
        assertThatThrownBy(() -> linkService.addLink(
            chatId,
            addLinkRequest
        )).isInstanceOf(LinkAlreadyTrackedException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void addLink_shouldThrowLinkNotSupportedException_whenLinkIsNotValid() {
        //Arrange
        String url = "google.com";
        Mockito.when(client.isValidated(URI.create(url))).thenReturn(false);
        Mockito.when(client.fetchData(URI.create(url)))
            .thenReturn(List.of(new LinkInfo(URI.create(url), "title", OffsetDateTime.now())));
        Long chatId = 1L;
        jdbcTemplate.update("INSERT INTO chat (chat_id) VALUES (?)", chatId);
        AddLinkRequest addLinkRequest = new AddLinkRequest(URI.create(url));
        //Expected
        assertThatThrownBy(() -> linkService.addLink(
            chatId,
            addLinkRequest
        )).isInstanceOf(LinkNotSupportedException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void addLink_shouldThrowChatNotAuthorizedException_whenChatIdIsNotInDb() {
        //Arrange
        String url = "google.com";
        Mockito.when(client.isValidated(URI.create(url))).thenReturn(false);
        Mockito.when(client.fetchData(URI.create(url)))
            .thenReturn(List.of(new LinkInfo(URI.create(url), "title", OffsetDateTime.now())));
        Long chatId = 1L;
        AddLinkRequest addLinkRequest = new AddLinkRequest(URI.create(url));
        //Expected
        assertThatThrownBy(() -> linkService.addLink(
            chatId,
            addLinkRequest
        )).isInstanceOf(ChatNotAuthorizedException.class);
    }
}
