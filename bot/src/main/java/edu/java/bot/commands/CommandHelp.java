package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import static edu.java.bot.utils.MessageConstants.DASH;
import static edu.java.bot.utils.MessageConstants.HELP_COMMAND;
import static edu.java.bot.utils.MessageConstants.HELP_COMMANDS_LIST;
import static edu.java.bot.utils.MessageConstants.HELP_DESCRIPTION;
import static edu.java.bot.utils.MessageConstants.HELP_WRONG_TEXT;

@Component
@RequiredArgsConstructor
public class CommandHelp implements Command {
    private final List<Command> commandsList;

    @Override
    public String command() {
        return HELP_COMMAND;
    }

    @Override
    public String description() {
        return HELP_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        if (update.message().text().equals(command())) {
            StringBuilder stringBuilder = new StringBuilder(HELP_COMMANDS_LIST);
            stringBuilder.append(command()).append(DASH).append(description()).append('\n');
            for (Command command : commandsList) {
                stringBuilder.append(command.command()).append(DASH).append(command.description()).append('\n');
            }
            return new SendMessage(chatId, stringBuilder.toString());
        }
        return new SendMessage(chatId, HELP_WRONG_TEXT);
    }
}
