package edu.java.scrapper.service;

import edu.java.client.ClientInfoProvider;
import edu.java.client.dto.LinkInfo;
import edu.java.client.github.GitHubInfoProvider;
import edu.java.dto.LinkData;
import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import edu.java.exceptions.ChatNotAuthorizedException;
import edu.java.exceptions.LinkAlreadyTrackedException;
import edu.java.exceptions.LinkNotSupportedException;
import edu.java.repository.chat.ChatRepository;
import edu.java.repository.chat.JdbcChatRepository;
import edu.java.repository.chat_link.ChatLinkRepository;
import edu.java.repository.chat_link.JdbcChatLinkRepository;
import edu.java.repository.link.JdbcLinkRepository;
import edu.java.repository.link.LinkRepository;
import edu.java.service.link.LinkService;
import edu.java.service.link.jdbc.JdbcLinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JdbcLinkServiceTest {
    @Test
    public void addLink_shouldReturnLinkResponse_whenLinkIsValidAndLinkWasNotInBd() {
        //Arrange
        long chatId = 1L;
        long linkId = 1L;
        URI url = URI.create("https://github.com/liljarn/tinkoffCourseJava2024");
        AddLinkRequest addLinkRequest =
            new AddLinkRequest(url);
        LinkRepository linkRepository = Mockito.mock(JdbcLinkRepository.class);
        Mockito.when(linkRepository.getLinkId(url.toString())).thenReturn(0L);
        Mockito.when(linkRepository.add(chatId, addLinkRequest)).thenReturn(new LinkResponse(linkId, url));
        ChatLinkRepository chatLinkRepository = Mockito.mock(JdbcChatLinkRepository.class);
        Mockito.doNothing().when(chatLinkRepository).add(chatId, linkId);
        ChatRepository chatRepository = Mockito.mock(JdbcChatRepository.class);
        Mockito.when(chatRepository.isInTable(chatId)).thenReturn(true);
        ClientInfoProvider client = Mockito.mock(GitHubInfoProvider.class);
        Mockito.when(client.isValidated(url)).thenReturn(true);
        Mockito.when(client.fetchData(url)).thenReturn(List.of(new LinkInfo(url, "title", OffsetDateTime.now())));
        LinkService service = new JdbcLinkService(linkRepository, chatLinkRepository, chatRepository, List.of(client));
        LinkResponse expected = new LinkResponse(linkId, url);
        //Act
        LinkResponse response = service.addLink(chatId, addLinkRequest);
        //Assert
        assertThat(response).isEqualTo(expected);
        Mockito.verify(chatLinkRepository, Mockito.times(1)).add(chatId, linkId);
    }

    @Test
    public void addLink_shouldThrowLinkNotSupportedException_whenRepositoryOrQuestionDoesNotExist() {
        //Arrange
        long chatId = 1L;
        long linkId = 1L;
        URI url = URI.create("https://github.com/liljarn/tinkoffCourseJava2024");
        AddLinkRequest addLinkRequest =
            new AddLinkRequest(url);
        LinkRepository linkRepository = Mockito.mock(JdbcLinkRepository.class);
        Mockito.when(linkRepository.getLinkId(url.toString())).thenReturn(0L);
        Mockito.when(linkRepository.add(chatId, addLinkRequest)).thenReturn(new LinkResponse(linkId, url));
        ChatLinkRepository chatLinkRepository = Mockito.mock(JdbcChatLinkRepository.class);
        Mockito.doNothing().when(chatLinkRepository).add(chatId, linkId);
        ChatRepository chatRepository = Mockito.mock(JdbcChatRepository.class);
        Mockito.when(chatRepository.isInTable(chatId)).thenReturn(true);
        ClientInfoProvider client = Mockito.mock(GitHubInfoProvider.class);
        Mockito.when(client.isValidated(url)).thenReturn(true);
        Mockito.doThrow(LinkNotSupportedException.class).when(client).fetchData(url);
        LinkService service = new JdbcLinkService(linkRepository, chatLinkRepository, chatRepository, List.of(client));
        //Expect
        assertThatThrownBy(() -> service.addLink(chatId, addLinkRequest)).isInstanceOf(LinkNotSupportedException.class);
    }

    @Test
    public void addLink_shouldThrowLinkNotSupportedException_whenLinkNotSupported() {
        //Arrange
        long chatId = 1L;
        URI url = URI.create("https://github.com/liljarn/tinkoffCourseJava2024");
        AddLinkRequest addLinkRequest =
            new AddLinkRequest(url);
        LinkRepository linkRepository = Mockito.mock(JdbcLinkRepository.class);
        ChatLinkRepository chatLinkRepository = Mockito.mock(JdbcChatLinkRepository.class);
        ChatRepository chatRepository = Mockito.mock(JdbcChatRepository.class);
        Mockito.when(chatRepository.isInTable(chatId)).thenReturn(true);
        ClientInfoProvider client = Mockito.mock(GitHubInfoProvider.class);
        Mockito.when(client.isValidated(url)).thenReturn(false);
        LinkService service = new JdbcLinkService(linkRepository, chatLinkRepository, chatRepository, List.of(client));
        //Expect
        assertThatThrownBy(() -> service.addLink(chatId, addLinkRequest)).isInstanceOf(LinkNotSupportedException.class);
    }

    @Test
    public void addLink_shouldThrowLinkAlreadyTrackedException_whenLinkExistsInBd() {
        //Arrange
        long chatId = 1L;
        long linkId = 1L;
        URI url = URI.create("https://github.com/liljarn/tinkoffCourseJava2024");
        AddLinkRequest addLinkRequest =
            new AddLinkRequest(url);
        LinkRepository linkRepository = Mockito.mock(JdbcLinkRepository.class);
        Mockito.when(linkRepository.getLinkId(url.toString())).thenReturn(chatId);
        Mockito.when(linkRepository.add(chatId, addLinkRequest)).thenReturn(new LinkResponse(linkId, url));
        ChatLinkRepository chatLinkRepository = Mockito.mock(JdbcChatLinkRepository.class);
        Mockito.doNothing().when(chatLinkRepository).add(chatId, linkId);
        Mockito.when(chatLinkRepository.isTracked(chatId, linkId)).thenReturn(true);
        ChatRepository chatRepository = Mockito.mock(JdbcChatRepository.class);
        Mockito.when(chatRepository.isInTable(chatId)).thenReturn(true);
        ClientInfoProvider client = Mockito.mock(GitHubInfoProvider.class);
        Mockito.when(client.isValidated(url)).thenReturn(true);
        Mockito.when(client.fetchData(url)).thenReturn(List.of(new LinkInfo(url, "title", OffsetDateTime.now())));
        LinkService service = new JdbcLinkService(linkRepository, chatLinkRepository, chatRepository, List.of(client));
        //Expect
        assertThatThrownBy(() -> service.addLink(
            chatId,
            addLinkRequest
        )).isInstanceOf(LinkAlreadyTrackedException.class);
    }

    @Test
    public void addLink_shouldReturnLinkResponse_whenLinkIsValidAndLinkWasInBd() {
        //Arrange
        long chatId = 1L;
        long linkId = 1L;
        URI url = URI.create("https://github.com/liljarn/tinkoffCourseJava2024");
        AddLinkRequest addLinkRequest =
            new AddLinkRequest(url);
        LinkRepository linkRepository = Mockito.mock(JdbcLinkRepository.class);
        Mockito.when(linkRepository.getLinkId(url.toString())).thenReturn(chatId);
        Mockito.when(linkRepository.add(chatId, addLinkRequest)).thenReturn(new LinkResponse(linkId, url));
        Mockito.when(linkRepository.getData(linkId)).thenReturn(new LinkData(OffsetDateTime.now(), url));
        ChatLinkRepository chatLinkRepository = Mockito.mock(JdbcChatLinkRepository.class);
        Mockito.doNothing().when(chatLinkRepository).add(chatId, linkId);
        Mockito.when(chatLinkRepository.isTracked(chatId, linkId)).thenReturn(false);
        ChatRepository chatRepository = Mockito.mock(JdbcChatRepository.class);
        Mockito.when(chatRepository.isInTable(chatId)).thenReturn(true);
        ClientInfoProvider client = Mockito.mock(GitHubInfoProvider.class);
        Mockito.when(client.isValidated(url)).thenReturn(true);
        Mockito.when(client.fetchData(url)).thenReturn(List.of(new LinkInfo(url, "title", OffsetDateTime.now())));
        LinkService service = new JdbcLinkService(linkRepository, chatLinkRepository, chatRepository, List.of(client));
        LinkResponse expected = new LinkResponse(linkId, url);
        //Act
        LinkResponse response = service.addLink(chatId, addLinkRequest);
        //Assert
        assertThat(response).isEqualTo(expected);
        Mockito.verify(chatLinkRepository, Mockito.times(1)).add(chatId, linkId);
    }

    @Test
    public void addLink_shouldThrowChatNotAuthorizedException_whenChatWasNotInBd() {
        //Arrange
        long chatId = 1L;
        URI url = URI.create("https://github.com/liljarn/tinkoffCourseJava2024");
        AddLinkRequest addLinkRequest =
            new AddLinkRequest(url);
        LinkRepository linkRepository = Mockito.mock(JdbcLinkRepository.class);
        ChatLinkRepository chatLinkRepository = Mockito.mock(JdbcChatLinkRepository.class);
        ChatRepository chatRepository = Mockito.mock(JdbcChatRepository.class);
        Mockito.when(chatRepository.isInTable(chatId)).thenReturn(false);
        ClientInfoProvider client = Mockito.mock(GitHubInfoProvider.class);
        LinkService service = new JdbcLinkService(linkRepository, chatLinkRepository, chatRepository, List.of(client));
        //Expect
        assertThatThrownBy(() -> service.addLink(
            chatId,
            addLinkRequest
        )).isInstanceOf(ChatNotAuthorizedException.class);
    }

    @Test
    public void deleteLink_shouldReturnLinkResponseAndRemoveLinkFromLinkTable_whenNobodyTracksIt() {
        //Arrange
        long chatId = 1L;
        long linkId = 1L;
        URI url = URI.create("google.com");
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        LinkRepository linkRepository = Mockito.mock(JdbcLinkRepository.class);
        Mockito.when(linkRepository.remove(chatId, removeLinkRequest)).thenReturn(new LinkResponse(linkId, url));
        ChatLinkRepository chatLinkRepository = Mockito.mock(JdbcChatLinkRepository.class);
        Mockito.when(chatLinkRepository.remove(chatId, linkId)).thenReturn(new LinkResponse(linkId, url));
        Mockito.when(chatLinkRepository.hasChats(linkId)).thenReturn(false);
        ChatRepository chatRepository = Mockito.mock(JdbcChatRepository.class);
        ClientInfoProvider client = Mockito.mock(GitHubInfoProvider.class);
        LinkService service = new JdbcLinkService(linkRepository, chatLinkRepository, chatRepository, List.of(client));
        LinkResponse expected = new LinkResponse(linkId, url);
        //Act
        LinkResponse response = service.deleteLink(chatId, removeLinkRequest);
        //Assert
        assertThat(response).isEqualTo(expected);
        Mockito.verify(linkRepository, Mockito.times(1)).remove(chatId, removeLinkRequest);
    }

    @Test
    public void deleteLink_shouldReturnLinkResponse_whenAnybodyTracksIt() {
        //Arrange
        long chatId = 1L;
        long linkId = 1L;
        URI url = URI.create("google.com");
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        LinkRepository linkRepository = Mockito.mock(JdbcLinkRepository.class);
        Mockito.when(linkRepository.remove(chatId, removeLinkRequest)).thenReturn(new LinkResponse(linkId, url));
        ChatLinkRepository chatLinkRepository = Mockito.mock(JdbcChatLinkRepository.class);
        Mockito.when(chatLinkRepository.remove(chatId, linkId)).thenReturn(new LinkResponse(linkId, url));
        Mockito.when(chatLinkRepository.hasChats(linkId)).thenReturn(true);
        ChatRepository chatRepository = Mockito.mock(JdbcChatRepository.class);
        ClientInfoProvider client = Mockito.mock(GitHubInfoProvider.class);
        LinkService service = new JdbcLinkService(linkRepository, chatLinkRepository, chatRepository, List.of(client));
        LinkResponse expected = new LinkResponse(linkId, url);
        //Act
        LinkResponse response = service.deleteLink(chatId, removeLinkRequest);
        //Assert
        assertThat(response).isEqualTo(expected);
        Mockito.verify(linkRepository, Mockito.times(0)).remove(chatId, removeLinkRequest);
    }
}
