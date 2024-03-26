package edu.java.scrapper.service;

import edu.java.exceptions.ChatAlreadyRegisteredException;
import edu.java.exceptions.ChatNotFoundException;
import edu.java.repository.chat.ChatRepository;
import edu.java.repository.chat.JdbcChatRepository;
import edu.java.service.chat.ChatService;
import edu.java.service.chat.JdbcChatService;
import org.junit.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JdbcChatServiceTest {
    @Test
    public void registerChat_shouldCallChatRepositoryAddMethod_whenChatNotInTable() {
        //Arrange
        long chatId = 1L;
        ChatRepository repository = Mockito.mock(JdbcChatRepository.class);
        Mockito.when(repository.isInTable(chatId)).thenReturn(false);
        Mockito.doNothing().when(repository).add(chatId);
        ChatService service = new JdbcChatService(repository);
        //Act
        service.registerChat(chatId);
        //Assert
        Mockito.verify(repository, Mockito.times(1)).add(chatId);
    }

    @Test
    public void registerChat_shouldThrowChatAlreadyRegisteredException_whenChatInTable() {
        //Arrange
        long chatId = 1L;
        ChatRepository repository = Mockito.mock(JdbcChatRepository.class);
        Mockito.when(repository.isInTable(chatId)).thenReturn(true);
        Mockito.doNothing().when(repository).add(chatId);
        ChatService service = new JdbcChatService(repository);
        //Act Expect
        assertThatThrownBy(() -> service.registerChat(chatId)).isInstanceOf(ChatAlreadyRegisteredException.class);
        //Assert
        Mockito.verify(repository, Mockito.times(0)).add(chatId);
    }

    @Test
    public void deleteChat_shouldCallChatRepositoryRemoveMethod_whenChatIdInTable() {
        //Arrange
        long chatId = 1L;
        ChatRepository repository = Mockito.mock(JdbcChatRepository.class);
        Mockito.when(repository.isInTable(chatId)).thenReturn(true);
        Mockito.doNothing().when(repository).remove(chatId);
        ChatService service = new JdbcChatService(repository);
        //Act
        service.deleteChat(chatId);
        //Assert
        Mockito.verify(repository, Mockito.times(1)).remove(chatId);
    }

    @Test
    public void deleteChat_shouldThrowChatNotFoundException_whenChatIdIsNotInTable() {
        //Arrange
        long chatId = 1L;
        ChatRepository repository = Mockito.mock(JdbcChatRepository.class);
        Mockito.when(repository.isInTable(chatId)).thenReturn(false);
        Mockito.doNothing().when(repository).remove(chatId);
        ChatService service = new JdbcChatService(repository);
        //Act Expect
        assertThatThrownBy(() -> service.deleteChat(chatId)).isInstanceOf(ChatNotFoundException.class);
        //Assert
        Mockito.verify(repository, Mockito.times(0)).remove(chatId);
    }
}
