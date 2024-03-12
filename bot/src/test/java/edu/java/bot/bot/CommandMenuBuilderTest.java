package edu.java.bot.bot;

import com.pengrad.telegrambot.model.BotCommand;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandStart;
import edu.java.bot.menu.CommandMenuBuilder;
import edu.java.bot.service.command.CommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import static edu.java.bot.utils.MessageConstants.START_COMMAND;
import static edu.java.bot.utils.MessageConstants.START_DESCRIPTION;
import static org.assertj.core.api.Assertions.assertThat;

public class CommandMenuBuilderTest {
    @Test
    @DisplayName("getCommandsMenu test")
    public void getCommandsMenu_shouldReturnCommandMenu() {
        //Arrange
        CommandService service = Mockito.mock(CommandService.class);
        Mockito.doNothing().when(service).registerChat(1L);
        List<Command> commands = List.of(new CommandStart(service));
        CommandMenuBuilder builder = new CommandMenuBuilder(commands);
        BotCommand botCommand = new BotCommand(START_COMMAND, START_DESCRIPTION);
        //Act
        BotCommand[] menuCommands = (BotCommand[]) builder.getCommandsMenu().getParameters().get("commands");
        //Assert
        assertThat(menuCommands[0]).isEqualTo(botCommand);
    }
}
