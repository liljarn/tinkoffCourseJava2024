package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.keyboard.InlineKeyboardBuilder;
import edu.java.bot.model.Link;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import static edu.java.bot.utils.MessageConsts.GITHUB_LINK;
import static edu.java.bot.utils.MessageConsts.LIST_COMMAND;
import static edu.java.bot.utils.MessageConsts.LIST_COMMANDS_TEXT;
import static edu.java.bot.utils.MessageConsts.LIST_DESCRIPTION;
import static edu.java.bot.utils.MessageConsts.STACK_LINK;

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
        return new SendMessage(chatId, LIST_COMMANDS_TEXT)
            .replyMarkup(InlineKeyboardBuilder.createUrlKeyboard(List.of(
                new Link(new UUID(1, 2), GITHUB_LINK),
                new Link(new UUID(2, 2), STACK_LINK)
            )));
    }
}
