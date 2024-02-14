package edu.java.bot.hw1;

import com.pengrad.telegrambot.model.BotCommand;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandStart;
import edu.java.bot.menu.CommandMenuBuilder;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static edu.java.bot.utils.MessageConstants.START_COMMAND;
import static edu.java.bot.utils.MessageConstants.START_DESCRIPTION;
import static org.assertj.core.api.Assertions.assertThat;

public class CommandMenuBuilderTest {
    @Test
    @DisplayName("getCommandsMenu test")
    public void getCommandsMenu_shouldReturnCommandMenu() {
        List<Command> commands = List.of(new CommandStart());
        CommandMenuBuilder builder = new CommandMenuBuilder(commands);
        BotCommand botCommand = new BotCommand(START_COMMAND, START_DESCRIPTION);
        BotCommand[] menuCommands = (BotCommand[]) builder.getCommandsMenu().getParameters().get("commands");
        assertThat(menuCommands[0]).isEqualTo(botCommand);
    }
}
