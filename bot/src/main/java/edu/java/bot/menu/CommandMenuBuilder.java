package edu.java.bot.menu;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.commands.Command;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandMenuBuilder {
    private final List<Command> commandsList;

    public SetMyCommands getCommandsMenu() {
        List<BotCommand> botCommands = commandsList.stream().map(Command::toApiCommand).toList();
        return new SetMyCommands(botCommands.toArray(new BotCommand[0]));
    }
}
