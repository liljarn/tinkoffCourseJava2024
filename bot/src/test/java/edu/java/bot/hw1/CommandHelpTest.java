package edu.java.bot.hw1;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandHelp;
import edu.java.bot.commands.CommandList;
import edu.java.bot.commands.CommandStart;
import java.util.List;
import edu.java.bot.commands.CommandTrack;
import edu.java.bot.commands.CommandUntrack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static edu.java.bot.utils.MessageConstants.HELP_COMMAND;
import static edu.java.bot.utils.MessageConstants.HELP_WRONG_TEXT;
import static org.assertj.core.api.Assertions.assertThat;

public class CommandHelpTest {
    @Test
    @DisplayName("Command /help test")
    public void handle_returnsHelpListMessage_whenCommandIsCorrect() {
        Update mockUpdate = TestUtils.createMockUpdate(HELP_COMMAND, 1L);
        List<Command> commandsList =
            List.of(new CommandStart(), new CommandList(), new CommandTrack(), new CommandUntrack());
        Command command = new CommandHelp(commandsList);
        SendMessage message = command.handle(mockUpdate);
        assertThat(message.getParameters().get("text")).isEqualTo(TestUtils.COMMANDS);
    }

    @Test
    @DisplayName("Wrong command /help test")
    public void handle_returnsWrongMessage_whenCommandIsWrong() {
        Update mockUpdate = TestUtils.createMockUpdate(HELP_COMMAND + "smth", 1L);
        List<Command> commandsList = List.of(new CommandStart());
        Command command = new CommandHelp(commandsList);
        SendMessage message = command.handle(mockUpdate);
        assertThat(message.getParameters().get("text")).isEqualTo(HELP_WRONG_TEXT);
    }
}
