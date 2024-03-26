package edu.java.bot.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandTrack;
import edu.java.bot.dto.client.AddLinkRequest;
import edu.java.bot.dto.client.LinkResponse;
import edu.java.bot.exception.ScrapperException;
import edu.java.bot.service.command.CommandService;
import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import static edu.java.bot.bot.TestUtils.GITHUB_LINK;
import static edu.java.bot.utils.MessageConstants.TRACK_COMMAND;
import static edu.java.bot.utils.MessageConstants.TRACK_COMMAND_ONLY;
import static edu.java.bot.utils.MessageConstants.TRACK_WRONG_TEXT;
import static org.assertj.core.api.Assertions.assertThat;

public class CommandTrackTest {
    @Test
    @DisplayName("Command /track test")
    public void handle_returnsMessage_whenOnlyCommandWasSent() {
        //Arrange
        CommandService service = Mockito.mock(CommandService.class);
        Mockito.when(service.addLink(1L, new AddLinkRequest(URI.create(GITHUB_LINK))))
            .thenReturn(new LinkResponse(1L, URI.create(GITHUB_LINK)));
        Update mockUpdate = TestUtils.createMockUpdate(TRACK_COMMAND, 1L);
        Command command = new CommandTrack(service);
        //Act
        SendMessage message = command.handle(mockUpdate);
        //Assert
        assertThat(message.getParameters().get("text")).isEqualTo(TRACK_COMMAND_ONLY);
    }

    @Test
    @DisplayName("Command /track with link test")
    public void handle_returnsOkMessage_whenCommandAndLinkWereSent() {
        //Arrange
        CommandService service = Mockito.mock(CommandService.class);
        Mockito.when(service.addLink(1L, new AddLinkRequest(URI.create(GITHUB_LINK))))
            .thenReturn(new LinkResponse(1L, URI.create(GITHUB_LINK)));
        Update mockUpdate = TestUtils.createMockUpdate(TRACK_COMMAND + " " + GITHUB_LINK, 1L);
        Command command = new CommandTrack(service);
        //Act
        SendMessage message = command.handle(mockUpdate);
        //Assert
        assertThat(message.getParameters().get("text"))
            .isEqualTo("Вебсайт " + GITHUB_LINK + " теперь отслеживается.");
    }

    @Test
    @DisplayName("Command /track with link test")
    public void handle_returnsExceptionMessage_whenExceptionWasThrown() {
        //Arrange
        CommandService service = Mockito.mock(CommandService.class);
        ScrapperException e = new ScrapperException("desc", HttpStatus.CONFLICT, "message");
        Mockito.doThrow(e).when(service).addLink(1L, new AddLinkRequest(URI.create(GITHUB_LINK)));
        Update mockUpdate = TestUtils.createMockUpdate(TRACK_COMMAND + " " + GITHUB_LINK, 1L);
        Command command = new CommandTrack(service);
        //Act
        SendMessage message = command.handle(mockUpdate);
        //Assert
        assertThat(message.getParameters().get("text"))
            .isEqualTo(e.getMessage());
    }

    @Test
    @DisplayName("Wrong command /track test")
    public void handle_returnsWrongMessage_whenWrongCommandWereSent() {
        //Arrange
        CommandService service = Mockito.mock(CommandService.class);
        Mockito.when(service.addLink(1L, new AddLinkRequest(URI.create(GITHUB_LINK))))
            .thenReturn(new LinkResponse(1L, URI.create(GITHUB_LINK)));
        Update mockUpdate = TestUtils.createMockUpdate(TRACK_COMMAND + "smth", 1L);
        Command command = new CommandTrack(service);
        //Act
        SendMessage message = command.handle(mockUpdate);
        //Assert
        assertThat(message.getParameters().get("text")).isEqualTo(TRACK_WRONG_TEXT);
    }
}
