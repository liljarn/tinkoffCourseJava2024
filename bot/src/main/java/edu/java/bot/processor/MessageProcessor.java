package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandUntrack;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import static edu.java.bot.utils.MessageConstants.NOT_COMMAND;
import static edu.java.bot.utils.MessageConstants.NOT_TEXT;

@Component
@RequiredArgsConstructor
@Log4j2
public class MessageProcessor implements Processor {
    private final List<Command> commandsList;

    @Override
    public SendMessage process(Update update) {
        if (update.callbackQuery() != null) {
               return processCallback(update);
        }
        return processMessage(update);
    }

    private SendMessage processCallback(Update update) {
        for (Command command : commandsList) {
            if (command instanceof CommandUntrack) {
                return command.handle(update);
            }
        }
        return null;
    }

    private SendMessage processMessage(Update update) {
        long chatId = update.message().chat().id();
        if (update.message().text() != null)  {
            for (Command command : commandsList) {
                if (command.supports(update)) {
                    return command.handle(update);
                }
            }
            return new SendMessage(chatId, NOT_COMMAND);
        }
        return new SendMessage(chatId, NOT_TEXT);
    }
}
