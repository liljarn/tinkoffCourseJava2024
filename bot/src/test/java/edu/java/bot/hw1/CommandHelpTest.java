package edu.java.bot.hw1;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandHelp;
import edu.java.bot.commands.CommandStart;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static edu.java.bot.utils.MessageConsts.HELP_COMMAND;
import static edu.java.bot.utils.MessageConsts.HELP_COMMANDS_LIST;
import static edu.java.bot.utils.MessageConsts.HELP_WRONG_TEXT;
import static edu.java.bot.utils.MessageConsts.START_COMMAND;
import static edu.java.bot.utils.MessageConsts.START_DESCRIPTION;
import static org.assertj.core.api.Assertions.assertThat;

public class CommandHelpTest {
    @Test
    @DisplayName("Command /help test")
    public void handle_returnsHelpListMessage_whenCommandIsCorrect() {
        Update mockUpdate = TestUtils.createMockUpdate(HELP_COMMAND, 1L);
        List<Command> commandsList = List.of(new CommandStart());
        Command command = new CommandHelp(commandsList);
        SendMessage message = command.handle(mockUpdate);
        assertThat(message.getParameters().get("text")).isEqualTo(
            HELP_COMMANDS_LIST + START_COMMAND + " — " + START_DESCRIPTION + "\n");
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
