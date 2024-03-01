package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.keyboard.InlineKeyboardBuilder;
import edu.java.bot.model.Link;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import static edu.java.bot.utils.MessageConstants.GITHUB_LINK;
import static edu.java.bot.utils.MessageConstants.LIST_COMMAND;
import static edu.java.bot.utils.MessageConstants.LIST_COMMANDS_TEXT;
import static edu.java.bot.utils.MessageConstants.LIST_DESCRIPTION;
import static edu.java.bot.utils.MessageConstants.LIST_WRONG_TEXT;
import static edu.java.bot.utils.MessageConstants.STACK_LINK;

@Component
public class CommandList implements Command {
    @Override
    public String command() {
        return LIST_COMMAND;
    }

    @Override
    public String description() {
        return LIST_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        if (update.message().text().equals(command())) {
            return new SendMessage(chatId, LIST_COMMANDS_TEXT)
                .replyMarkup(InlineKeyboardBuilder.createUrlKeyboard(List.of(
                    new Link(new UUID(1, 2), GITHUB_LINK),
                    new Link(new UUID(2, 2), STACK_LINK)
                )));
        }
        return new SendMessage(chatId, LIST_WRONG_TEXT);
    }
}
