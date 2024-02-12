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
            return new SendMessage(
                chatId,
                "Не понимаю :(, напишите команду /help для вывода *списка* доступных команд."
            );
        }
        return new SendMessage(
            chatId,
            "*Пожалуйста*, не отправляйте что-либо кроме текста, мне тяжело это обрабатывать :("
        );
    }
}
