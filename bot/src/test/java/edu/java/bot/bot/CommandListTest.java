package edu.java.bot.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandList;
import edu.java.bot.dto.client.ListLinksResponse;
import edu.java.bot.service.command.CommandService;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static edu.java.bot.utils.MessageConstants.LIST_COMMAND;
import static edu.java.bot.utils.MessageConstants.LIST_COMMANDS_TEXT;
import static edu.java.bot.utils.MessageConstants.LIST_WRONG_TEXT;
import static org.assertj.core.api.Assertions.assertThat;

public class CommandListTest {
    @Test
    @DisplayName("Command /list test")
    public void handle_returnsListMessage_whenCommandIsCorrect() {
        //Arrange
        CommandService service = Mockito.mock(CommandService.class);
        Mockito.when(service.getLinks(1L)).thenReturn(new ListLinksResponse(Collections.EMPTY_LIST, 0));
        Update mockUpdate = TestUtils.createMockUpdate(LIST_COMMAND, 1L);
        Command command = new CommandList(service);
        //Act
        SendMessage message = command.handle(mockUpdate);
        //Assert
        assertThat(message.getParameters().get("text")).isEqualTo(LIST_COMMANDS_TEXT);
    }

    @Test
    @DisplayName("Wrong command /list test")
    public void handle_returnsWrongMessage_whenCommandIsWrong() {
        //Arrange
        CommandService service = Mockito.mock(CommandService.class);
        Mockito.when(service.getLinks(1L)).thenReturn(new ListLinksResponse(Collections.EMPTY_LIST, 0));
        Update mockUpdate = TestUtils.createMockUpdate(LIST_COMMAND + "smth", 1L);
        Command command = new CommandList(service);
        //Act
        SendMessage message = command.handle(mockUpdate);
        //Assert
        assertThat(message.getParameters().get("text")).isEqualTo(LIST_WRONG_TEXT);
    }
}
