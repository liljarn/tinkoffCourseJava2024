package edu.java.bot.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandStart;
import edu.java.bot.service.command.CommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static edu.java.bot.utils.MessageConstants.START_COMMAND;
import static edu.java.bot.utils.MessageConstants.START_MESSAGE;
import static edu.java.bot.utils.MessageConstants.START_WRONG_TEXT;
import static org.assertj.core.api.Assertions.assertThat;

public class CommandStartTest {
    @Test
    @DisplayName("Command /start test")
    public void handle_returnsGreetingMessage_whenCommandIsCorrect() {
        //Arrange
        CommandService service = Mockito.mock(CommandService.class);
        Mockito.doNothing().when(service).registerChat(1L);
        Update mockUpdate = TestUtils.createMockUpdate(START_COMMAND, 1L);
        Command command = new CommandStart(service);
        //Act
        SendMessage message = command.handle(mockUpdate);
        //Assert
        assertThat(message.getParameters().get("text")).isEqualTo(START_MESSAGE);
    }

    @Test
    @DisplayName("Wrong command /start test")
    public void handle_returnsWrongMessage_whenCommandIsWrong() {
        //Arrange
        CommandService service = Mockito.mock(CommandService.class);
        Mockito.doNothing().when(service).registerChat(1L);
        Update mockUpdate = TestUtils.createMockUpdate("/startsmth", 1L);
        Command command = new CommandStart(service);
        //Act
        SendMessage message = command.handle(mockUpdate);
        //Assert
        assertThat(message.getParameters().get("text")).isEqualTo(START_WRONG_TEXT);
    }
}
