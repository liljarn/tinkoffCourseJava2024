package edu.java.bot.bot;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandUntrack;
import edu.java.bot.dto.client.AddLinkRequest;
import edu.java.bot.dto.client.LinkResponse;
import edu.java.bot.dto.client.ListLinksResponse;
import edu.java.bot.dto.client.RemoveLinkRequest;
import edu.java.bot.exception.ScrapperException;
import edu.java.bot.service.command.CommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import java.net.URI;
import java.util.List;
import static edu.java.bot.bot.TestUtils.GITHUB_LINK;
import static edu.java.bot.utils.MessageConstants.SUCCESSFUL_DELETE;
import static edu.java.bot.utils.MessageConstants.UNTRACK_COMMAND;
import static edu.java.bot.utils.MessageConstants.UNTRACK_MESSAGE;
import static edu.java.bot.utils.MessageConstants.UNTRACK_WRONG_TEXT;
import static org.assertj.core.api.Assertions.assertThat;

public class CommandUntrackTest {
    @Test
    @DisplayName("Command /untrack test")
    public void handle_returnsMessage_whenMessageCommandWasSent() {
        //Arrange
        CommandService service = Mockito.mock(CommandService.class);
        Mockito.when(service.getLinks(1L))
            .thenReturn(new ListLinksResponse(List.of(new LinkResponse(1L, URI.create("google.com"))), 1));
        Update mockUpdate = TestUtils.createMockUpdate(UNTRACK_COMMAND, 1L);
        Command command = new CommandUntrack(service);
        //Act
        SendMessage message = command.handle(mockUpdate);
        //Assert
        assertThat(message.getParameters().get("text")).isEqualTo(UNTRACK_MESSAGE);
    }

    @Test
    @DisplayName("Command /untrack test with exception")
    public void handle_returnsExceptionMessage_whenExceptionWasThrown() {
        //Arrange
        CommandService service = Mockito.mock(CommandService.class);
        ScrapperException e = new ScrapperException("desc", HttpStatus.CONFLICT, "message");
        Mockito.doThrow(e).when(service).getLinks(1L);
        Update mockUpdate = TestUtils.createMockUpdate(UNTRACK_COMMAND, 1L);
        Command command = new CommandUntrack(service);
        //Act
        SendMessage message = command.handle(mockUpdate);
        //Assert
        assertThat(message.getParameters().get("text")).isEqualTo(e.getMessage());
    }

    @Test
    @DisplayName("Command /untrack test with callback")
    public void handle_returnsDeleteMessage_whenCallbackWasSent() {
        //Arrange
        CommandService service = Mockito.mock(CommandService.class);
        Mockito.when(service.removeLink(1L, new RemoveLinkRequest(1L)))
            .thenReturn(new LinkResponse(1L, URI.create("google.com")));
        Update callback = TestUtils.createMockCallbackUpdate(1L, "1");
        Command command = new CommandUntrack(service);
        //Act
        SendMessage message = command.handle(callback);
        //Assert
        assertThat(message.getParameters().get("text")).isEqualTo(SUCCESSFUL_DELETE);
    }

    @Test
    @DisplayName("Wrong command /untrack test")
    public void handle_returnsWrongMessage_whenWrongCommandMessageWasSent() {
        //Arrange
        CommandService service = Mockito.mock(CommandService.class);
        Mockito.when(service.getLinks(1L))
            .thenReturn(new ListLinksResponse(List.of(new LinkResponse(1L, URI.create("google.com"))), 1));
        Update mockUpdate = TestUtils.createMockUpdate(UNTRACK_COMMAND + "smth", 1L);
        Command command = new CommandUntrack(service);
        //Act
        SendMessage message = command.handle(mockUpdate);
        //Assert
        assertThat(message.getParameters().get("text")).isEqualTo(UNTRACK_WRONG_TEXT);
    }
}
