package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import static edu.java.bot.utils.MessageConsts.START_COMMAND;
import static edu.java.bot.utils.MessageConsts.START_DESCRIPTION;
import static edu.java.bot.utils.MessageConsts.START_MESSAGE;
import static edu.java.bot.utils.MessageConsts.START_WRONG_TEXT;

@Component
public class CommandStart implements Command {
    @Override
    public String command() {
        return START_COMMAND;
    }

    @Override
    public String description() {
        return START_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        if (update.message().text().equals(command())) {
            return new SendMessage(chatId, START_MESSAGE);
        }
        return new SendMessage(chatId, START_WRONG_TEXT);
    }
}
