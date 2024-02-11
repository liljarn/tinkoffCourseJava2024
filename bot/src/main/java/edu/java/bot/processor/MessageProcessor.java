package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class MessageProcessor implements UserMessageProcessor {
    private final List<Command> commandsList;

    @Override
    public List<Command> commands() {
        return commandsList;
    }

    @Override
    public SendMessage process(Update update) {
        long chatId = update.message().chat().id();
        for (Command command : commandsList) {
            if (command.supports(update)) {
                return command.handle(update);
            }
        }
        return new SendMessage(chatId, "Sorry, wrong command :(");
    }
}
