package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import static edu.java.bot.utils.MessageConsts.NOT_COMMAND;
import static edu.java.bot.utils.MessageConsts.NOT_TEXT;

@Component
@RequiredArgsConstructor
@Log4j2
public class MessageProcessor extends AbstractProcessor {
    private final List<Command> commandsList;

    @Override
    public SendMessage check(Update update) {
        if (update.message() != null) {
            return process(update);
        }
        return checkNext(update);
    }

    @Override
    public SendMessage process(Update update) {
        long chatId = update.message().chat().id();
        if (update.message().text() != null) {
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
