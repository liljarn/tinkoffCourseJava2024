package edu.java.bot.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandHelp;
import edu.java.bot.commands.CommandList;
import edu.java.bot.commands.CommandStart;
import edu.java.bot.commands.CommandTrack;
import edu.java.bot.commands.CommandUntrack;
import edu.java.bot.service.command.CommandService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static edu.java.bot.utils.MessageConstants.HELP_COMMAND;
import static edu.java.bot.utils.MessageConstants.HELP_WRONG_TEXT;
import static org.assertj.core.api.Assertions.assertThat;

public class CommandHelpTest {
    @Test
    @DisplayName("Command /help test")
    public void handle_returnsHelpListMessage_whenCommandIsCorrect() {
        //Arrange
        CommandService service = Mockito.mock(CommandService.class);
        Update mockUpdate = TestUtils.createMockUpdate(HELP_COMMAND, 1L);
        List<Command> commandsList =
            List.of(new CommandStart(service), new CommandList(service), new CommandTrack(service), new CommandUntrack());
        Command command = new CommandHelp(commandsList);
        //Act
        SendMessage message = command.handle(mockUpdate);
        //Assert
        assertThat(message.getParameters().get("text")).isEqualTo(TestUtils.COMMANDS);
    }

    @Test
    @DisplayName("Wrong command /help test")
    public void handle_returnsWrongMessage_whenCommandIsWrong() {
        //Arrange
        CommandService service = Mockito.mock(CommandService.class);
        Update mockUpdate = TestUtils.createMockUpdate(HELP_COMMAND + "smth", 1L);
        List<Command> commandsList = List.of(new CommandStart(service));
        Command command = new CommandHelp(commandsList);
        //Act
        SendMessage message = command.handle(mockUpdate);
        //Assert
        assertThat(message.getParameters().get("text")).isEqualTo(HELP_WRONG_TEXT);
    }
}
